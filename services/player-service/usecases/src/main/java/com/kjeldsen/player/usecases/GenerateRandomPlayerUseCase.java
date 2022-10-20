package com.kjeldsen.player.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAbility;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerTendency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class GenerateRandomPlayerUseCase {

    public void generate(int age, PlayerPosition position, PlayerTendency playerTendency, int points) {

        log();

        Player player = Player.builder()
            .id(String.valueOf(UUID.randomUUID()))
            .name("player name")
            .age(age)
            .position(position)
            .build();

        for (int i = 0; i < points; i++) {
            int p = 5;
            PlayerAbility playerAbility = PlayerAbility.GOAL;
            // TODO which ability and how many points?
            player.addAbilityPoints(playerAbility, p);
        }

        log.info("Generated player {}", player);
    }

    // TODO replace this by annotation to get free login
    private void log() {
        log.info("Calling {}#{}",
            this.getClass().getSimpleName(),
            this.getClass().getEnclosingMethod().getName());
    }

}
