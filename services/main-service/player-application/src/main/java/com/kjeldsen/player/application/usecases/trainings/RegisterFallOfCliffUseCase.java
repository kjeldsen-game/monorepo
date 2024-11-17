package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerFallOffCliffEvent;
import com.kjeldsen.player.domain.generator.FallOffCliffGenerator;
import com.kjeldsen.player.domain.repositories.PlayerFallOffCliffEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterFallOfCliffUseCase {

    private static final int FALL_OFF_CLIFF_TRIGGER_AGE = 30;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerFallOffCliffEventWriteRepository playerFallOffCliffEventWriteRepository;

    /*
    * RegisterFallOffCliffUseCase is use case defined for one time registering the fall off cliff for player.
    * Word "Register" meaning that this happened only once (per season not daily/weekly) and is done for
    * all players automatically via scheduler which have valid years (check FALL_OFF_CLIFF_TRIGGER_AGE)
    */

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
                    PlayerFallOffCliffEvent event = PlayerFallOffCliffEvent.builder()
                        .id(EventId.generate())
                        .occurredAt(Instant.now())
                        .playerId(player.getId()).build();

                    playerFallOffCliffEventWriteRepository.save(event);
                    playerWriteRepository.save(player);
                }
            }
        });
        log.info("RegisterFallOfCliffUseCase registered with {} falloffs for players!", successfulFallOffs.get()) ;
    }
}
