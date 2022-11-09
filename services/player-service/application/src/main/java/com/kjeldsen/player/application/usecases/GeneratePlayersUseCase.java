package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePlayersUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    public void generate(int numberOfPlayers) {
        log.info("Generating {} players", numberOfPlayers);
        IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> playerPositionTendencyReadRepository.get(PlayerPosition.random()))
            .map(positionTendencies -> Player.generate(positionTendencies, 200))
            .forEach(player -> {
                playerWriteRepository.save(player);
                log.info("Generated player {}", player);
            });
    }
}
