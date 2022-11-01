package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class CreatePlayerUseCase {

    private PlayerWriteRepository repository;

    public void create(NewPlayer newPlayer) {

        Player player = Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(newPlayer.getAge())
            .position(newPlayer.getPosition())
            .actualSkills(PlayerActualSkills.generate(newPlayer.getPosition(), newPlayer.getPoints()))
            .build();

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
