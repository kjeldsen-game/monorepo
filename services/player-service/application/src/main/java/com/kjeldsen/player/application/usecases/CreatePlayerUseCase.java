package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.application.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePlayerUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPositionTendencyReadRepository playerPositionTendencyReadRepository;

    public void create(NewPlayer newPlayer) {
        PlayerPositionTendency positionTendencies = playerPositionTendencyReadRepository.get(newPlayer.getPosition());
        Player player = Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(newPlayer.getAge())
            .position(newPlayer.getPosition())
            .actualSkills(PlayerActualSkills
                .generate(positionTendencies, newPlayer.getPoints()))
            .build();

        log.info("Generated player {}", player);
        playerWriteRepository.save(player);
    }

    // FIXME throw NullPointerException: getEnclosingMethod() is null
    // TODO replace this by annotation to get free login
    private void log() {
        log.info("Calling {}#{}",
            this.getClass().getSimpleName(),
            this.getClass().getEnclosingMethod().getName());
    }

}
