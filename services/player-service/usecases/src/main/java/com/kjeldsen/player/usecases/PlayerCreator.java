package com.kjeldsen.player.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAbilities;
import com.kjeldsen.player.domain.PlayerAbilities.PlayerAbility;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.usecases.annotation.UseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PlayerCreator {
    public void handle(PlayerCreatorCommand command) {

        log();

        Player player = Player.builder()
                .id(PlayerId.generate())
                .name(PlayerName.generate())
                .age(command.getAge())
                .position(command.getPosition())
                .abilities(PlayerAbilities.of(command.getPosition()))
                .build();

        for (int i = 0; i < command.getPoints(); i++) {
            int p = 5;
            PlayerAbility playerAbility = PlayerAbility.GOAL;
            // TODO which ability and how many points?
            player.getAbilities().addAbilityPoints(playerAbility, p);
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
