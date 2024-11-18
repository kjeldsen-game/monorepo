package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPotentialRiseUseCase {

    private static final Integer MAX_AGE = 21;
    private final PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    /*
    * ProcessPotentialRiseUseCase is use case that it's executed automatically by scheduler (quartz) and User/Team
    * have no power (cannot modify/schedule or chose) any configuration. Use case get all players which are under 21,
    * generate the rise (which could happened every day, there is no dependency for previous days yet), generate random skill.
    * Once the generate part is done, based on the results (if rise happened or not) the potential is updated and event saved.
    */

    public void process() {
        AtomicReference<Integer> failed = new AtomicReference<>(0);
        AtomicReference<Integer> successful = new AtomicReference<>(0);
        List<Player> players = playerReadRepository.findPlayerUnderAge(MAX_AGE);
        log.info("Running PotentialRiseUseCase for {} players", players.size());
        players.forEach(
            player -> {
                if (player.getAge().getYears() >= MAX_AGE) {
                    throw new IllegalArgumentException("The age of the player must be less than 21 years.");
                }
                Integer rise = PotentialRiseGenerator.generatePotentialRaise();
                PlayerSkill randomSkill = PlayerProvider.randomSkillForSpecificPlayer(player);
                if (rise != 0) { // Rise happened
                    successful.getAndSet(successful.get() + 1);
//                    log.info("Rise happened for player {} w points {} skill {}", player.getName(), rise, randomSkill);
                    executePlayerRiseAndStoreEvent(player, randomSkill, rise);
                } else {
                    failed.getAndSet(successful.get() + 1);
                }
            }
        );
        log.info("Finished running PotentialRiseUseCase for {} players successful {}, failed {}",
            players.size(), successful.get(), failed.get());
    }

    private void executePlayerRiseAndStoreEvent(Player player, PlayerSkill randomSkill,Integer rise) {
        PlayerPotentialRiseEvent playerPotentialRiseEvent = PlayerPotentialRiseEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .potentialBeforeRaise(player.getPotentialSkillPoints(randomSkill))
            .pointsToRise(rise)
            .actualPoints(player.getActualSkillPoints(randomSkill))
            .skillThatRisen(randomSkill)
            .build();

        player.addSkillsPotentialRisePoints(randomSkill, rise);
        playerPotentialRiseEvent.setPotentialAfterRaise(player.getPotentialSkillPoints(randomSkill));
        playerPotentialRiseEventWriteRepository.save(playerPotentialRiseEvent);
        playerWriteRepository.save(player);
    }
}