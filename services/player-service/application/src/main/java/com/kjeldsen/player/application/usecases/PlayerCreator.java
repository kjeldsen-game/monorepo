package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.application.shared.UseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAbilities;
import com.kjeldsen.player.domain.PlayerAbilities.PlayerAbility;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@UseCase
public class PlayerCreator {

    private PlayerWriteRepository repository;

    public void handle(PlayerCreatorCommand command) {

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
        repository.save(player);
    }

    // FIXME throw NullPointerException: getEnclosingMethod() is null
    // TODO replace this by annotation to get free login
    private void log() {
        log.info("Calling {}#{}",
                this.getClass().getSimpleName(),
                this.getClass().getEnclosingMethod().getName());
    }

}
