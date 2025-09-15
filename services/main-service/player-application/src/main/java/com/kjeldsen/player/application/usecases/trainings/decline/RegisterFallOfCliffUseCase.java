package com.kjeldsen.player.application.usecases.trainings.decline;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.generator.FallOffCliffGenerator;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
* Use case for registering "fall off cliff" events for players over a defined age threshold
* ({@link #FALL_OFF_CLIFF_TRIGGER_AGE}).
* Ensures the event is triggered only once per player and updates the {@link Player#isFallCliff()} status.
* Executed automatically by a scheduler to handle eligible players each season.
*/
@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterFallOfCliffUseCase {

    private static final int FALL_OFF_CLIFF_TRIGGER_AGE = 30;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public void register() {
        AtomicReference<Integer> successfulFallOffs = new AtomicReference<>(0);
        List<Player> players = playerReadRepository.findPlayerOverAge(FALL_OFF_CLIFF_TRIGGER_AGE);
        log.info("RegisterFallOfCliffUseCase: found {} players", players.size());

        players.forEach(player -> {
            if (player.getAge().getYears() < FALL_OFF_CLIFF_TRIGGER_AGE) {
                throw new IllegalArgumentException("Invalid Player Age");
            }

            if (!player.isFallCliff()) {
                boolean isFallOff = FallOffCliffGenerator.checkIfFallOffCliffHappened(player.getAge().getYears());
                if (isFallOff) {
                    successfulFallOffs.getAndSet(successfulFallOffs.get() + 1);
                    player.setFallCliff(true);
                    playerWriteRepository.save(player);
                }
            }
        });
        log.info("RegisterFallOfCliffUseCase registered with {} falloffs for players!", successfulFallOffs.get()) ;
    }
}
