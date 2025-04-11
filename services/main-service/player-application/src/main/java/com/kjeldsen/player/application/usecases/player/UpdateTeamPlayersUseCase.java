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
    private final PlayerWriteRepository playerWriteRepository;
    private final GetPlayersUseCase getPlayersUseCase;

    public Team update(List<PlayerEdit> playerEdits, String teamId) {

        log.info("UpdateTeamPlayersUseCase for team {} for {} players", teamId, playerEdits.size());

        List<Player> teamPlayers = getPlayersUseCase.get(teamId);
        playerEdits.forEach(update -> {
            Player player = teamPlayers.stream().filter(p ->
                Objects.equals(p.getId().value(), update.id())).findAny().orElseThrow();

            player.setPlayerOrder(PlayerOrder.valueOf(update.playerOrder().name()));
            player.setStatus(PlayerStatus.valueOf(update.status().name()));
            if (update.playerOrderDestinationPitchArea() != null) {
                player.setPlayerOrderDestinationPitchArea(PitchArea.valueOf(update.playerOrderDestinationPitchArea().name()));
            }
            player.setPosition(update.position() != null ? PlayerPosition.valueOf(update.position().name()) : null);
            playerWriteRepository.save(player);
        });

        return teamReadRepository.findById(Team.TeamId.of(teamId)).orElseThrow();
    }

    public record PlayerEdit(String id, PlayerStatus status, PlayerPosition position, PlayerOrder playerOrder, PitchArea playerOrderDestinationPitchArea) {
    }
}
