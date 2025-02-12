package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateTeamPlayersUseCase {

    private final TeamReadRepository teamReadRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public Team update(TeamModifiers teamModifiers, List<PlayerEdit> playerEdits, String teamId) {

        log.info("UpdateTeamPlayersUseCase for team {} for {} players", teamId, playerEdits.size());

        List<PlayerEdit> activePlayers = playerEdits.stream()
            .filter(playerEdit -> playerEdit.status().equals(PlayerStatus.ACTIVE))
            .toList();
        log.info("{}",activePlayers.size());
        List<PlayerEdit> benchPlayers = playerEdits.stream()
            .filter(playerEdit -> playerEdit.status().equals(PlayerStatus.BENCH
            ))
            .toList();
        log.info("{}",benchPlayers.size());

        List<Player> teamPlayers = playerReadRepository.findByTeamId(Team.TeamId.of(teamId));
        playerEdits.forEach(update -> {
            Player player = teamPlayers.stream().filter(p ->
                Objects.equals(p.getId().value(), update.id())).findAny().orElseThrow();

            // TODO make a specific use case for all player position changes
            boolean playerChangingPositionFromGoalkeeper = PlayerPosition.GOALKEEPER.equals(player.getPosition()) && !PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.position().name()));
            boolean playerChangingPositionToGoalkeeper = !PlayerPosition.GOALKEEPER.equals(player.getPosition()) && PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.position().name()));
            if (playerChangingPositionFromGoalkeeper || playerChangingPositionToGoalkeeper) throw new RuntimeException(player.getName() + ": GOALKEEPER position is not changeable.");

            player.setPlayerOrder(PlayerOrder.valueOf(update.playerOrder().name()));
            player.setPosition(PlayerPosition.valueOf(update.position().name()));
            player.setStatus(PlayerStatus.valueOf(update.status().name()));
            if (update.playerOrderDestinationPitchArea() != null) {
                player.setPlayerOrderDestinationPitchArea(PitchArea.valueOf(update.playerOrderDestinationPitchArea().name()));
            }
            playerWriteRepository.save(player);
        });

        return teamReadRepository.findById(Team.TeamId.of(teamId)).orElseThrow();
    }

    public record PlayerEdit(String id, PlayerStatus status, PlayerPosition position, PlayerOrder playerOrder, PitchArea playerOrderDestinationPitchArea) {
    }
}
