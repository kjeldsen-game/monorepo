package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.aggregate.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PositionalDuelStartUseCase {

    public void start(Player attacker, Player defender) {
        log.info("Creating positional duel");

        // TODO: Create DuelStartedEvent and publish it
    }
}
