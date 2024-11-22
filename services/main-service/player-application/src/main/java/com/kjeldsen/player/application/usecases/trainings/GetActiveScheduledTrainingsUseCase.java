package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetActiveScheduledTrainingsUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;

    public List<PlayerScheduledTraining> get(Team.TeamId teamId) {
        List<Player> players = playerReadRepository.findByTeamId(teamId);
        List<PlayerScheduledTraining> playerScheduled = new ArrayList<>();
        List<PlayerTrainingScheduledEvent> activeTrainings = playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings();
        players.forEach(player -> {
           List<PlayerTrainingScheduledEvent> playerActiveTrainings = activeTrainings
                .stream().filter(event -> event.getPlayerId().equals(player.getId()))
                .sorted(Comparator.comparing(PlayerTrainingScheduledEvent::getOccurredAt))
                .toList();

            PlayerScheduledTraining playerScheduledTraining = PlayerScheduledTraining.builder()
                .player(player)
                .skills(playerActiveTrainings.stream().map(PlayerTrainingScheduledEvent::getSkill).toList())
                .build();

            playerScheduled.add(playerScheduledTraining);
        });
        return playerScheduled;
    }

    @Builder
    @Getter
    @Setter
    public static class PlayerScheduledTraining {
        private final Player player;
        private final List<PlayerSkill> skills;
    }
}
