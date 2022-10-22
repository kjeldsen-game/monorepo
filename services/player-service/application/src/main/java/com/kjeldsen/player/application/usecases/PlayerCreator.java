package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.application.shared.UseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.PlayerSkills.PlayerSkill;
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
                .skills(PlayerSkills.of(command.getPosition()))
                .build();

        for (int i = 0; i < command.getPoints(); i++) {
            int p = 5;
            PlayerSkill playerSkill = PlayerSkill.GOAL;
            // TODO which ability and how many points?
            player.getSkills().addAbilityPoints(playerSkill, p);
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
