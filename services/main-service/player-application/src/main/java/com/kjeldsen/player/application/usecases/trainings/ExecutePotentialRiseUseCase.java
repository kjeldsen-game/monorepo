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

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutePotentialRiseUseCase {

    private static final Integer MAX_AGE = 21;
    private final PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;
    private final PlayerWriteRepository playerWriteRepository;


    public void execute(Player player) {
        if (player.getAge().getYears() >= MAX_AGE) {
            throw new IllegalArgumentException("The age of the player must be less than 21 years.");
        }
        Integer rise = PotentialRiseGenerator.generatePotentialRaise();
        PlayerSkill randomSkill = PlayerProvider.randomSkillForSpecificPlayer(player);
        if (rise != 0) { // Rise happened
//            successful.getAndSet(successful.get() + 1);
                    log.info("Rise happened for player {} w points {} skill {}", player.getName(), rise, randomSkill);
            executePlayerRiseAndStoreEvent(player, randomSkill, rise);
        } else {
//            failed.getAndSet(successful.get() + 1);
        }
    }

    private void executePlayerRiseAndStoreEvent(Player player, PlayerSkill randomSkill, Integer rise) {
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
