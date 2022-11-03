package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Component
public class GeneratePlayersUseCase {

    private final PlayerWriteRepository playerWriteRepository;

    public void generate(int numberOfPlayers) {
        log.info("Generating {} players", numberOfPlayers);
        IntStream.range(0, numberOfPlayers)
            .mapToObj(i -> Player.generate(200))
            .forEach(player -> {
                playerWriteRepository.save(player);
                log.info("Generated player {}", player);
            });
    }
}
