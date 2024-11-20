package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetHistoricalTeamPlayerTrainingUseCase {

    private final PlayerTrainingEventReadRepository playerTrainingEventReadRepository;
    private final PlayerReadRepository playerReadRepository;

    public Map<String, List<PlayerTraining>> get(String teamId) {
        log.info("GetHistoricalPlayerTrainingUseCase for team {}", teamId);

        // Fetch players from the team
        List<Player> teamPlayers = playerReadRepository.findByTeamId(Team.TeamId.of(teamId));

        // List to accumulate PlayerTraining objects
        List<PlayerTraining> successfulEvents = new ArrayList<>();

        // Retrieve all training events for team
        teamPlayers.forEach(player -> {
            List<PlayerTrainingEvent> playerEvents = playerTrainingEventReadRepository
                .findAllSuccessfulByPlayerIdAndTeamId(player.getId(), Team.TeamId.of(teamId));

            // Create PlayerTraining for each event and add to the list
            playerEvents.forEach(playerTrainingEvent -> {
                PlayerTraining playerTraining = PlayerTraining.builder()
                    .player(Player.builder()
                        .id(player.getId())
                        .age(player.getAge())
                        .status(player.getStatus())
                        .name(player.getName())
                        .position(player.getPosition()).build())
                    .playerTrainingEvent(playerTrainingEvent)
                    .build();
                successfulEvents.add(playerTraining);
            });
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return successfulEvents.stream()
            .collect(Collectors.groupingBy(playerTraining ->
                playerTraining.playerTrainingEvent.getOccurredAt()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(formatter)
            ));
    }

    @Builder
    @Setter
    @Getter
    public static class PlayerTraining {
        private Player player;
        private PlayerTrainingEvent playerTrainingEvent;
    }
}
