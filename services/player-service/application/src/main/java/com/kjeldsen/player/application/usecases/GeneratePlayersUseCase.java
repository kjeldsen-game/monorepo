package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerCategory;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePlayersUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    public List<Player> generate(int numberOfPlayers, Team.TeamId teamId) {
        log.info("Generating {} players", numberOfPlayers);
        return IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> {
                PlayerPositionTendency positionTendency = playerPositionTendencyReadRepository.get(PlayerProvider.position());
                PlayerCategory playerCategory = i >= numberOfPlayers / 2 ? PlayerCategory.JUNIOR : PlayerCategory.SENIOR;
                return PlayerProvider.generate(teamId, positionTendency, playerCategory, 200);
            })
            .map(player -> {
                // TODO 72-add-potentials-to-the-player change this code to call CreatePlayerUseCase. We might mapper from player to NewPlayer and
                //  maybe more changes
                Player generatedPlayer = playerWriteRepository.save(player);
                log.info("Generated player {}", generatedPlayer);
                return generatedPlayer;
            }).toList();
    }
}
