package com.kjeldsen.player.application.usecases.trainings.potential;

import com.kjeldsen.player.application.usecases.trainings.BaseTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
* ProcessPotentialRiseUseCase is use case that it's executed automatically by scheduler (quartz) and User/Team
* have no power (cannot modify/schedule or chose) any configuration. Use case get all players which are under 21,
* generate the rise (which could happened every day, there is no dependency for previous days), generate random skill.
* Once the generate part is done, based on the results (if rise happened or not) the potential is updated and event saved.
*/
@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPotentialRiseUseCase extends BaseTrainingUseCase {

    private static final Integer MAX_AGE = 21;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

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

                    TrainingEvent event = buildTrainingEvent(player, randomSkill, rise, TrainingEvent.TrainingType.POTENTIAL_RISE, false);
                    event.setPointsBeforeTraining(player.getPotentialSkillPoints(randomSkill));
                    player.addSkillsPotentialRisePoints(randomSkill, rise);
                    event.setPointsAfterTraining(player.getPotentialSkillPoints(randomSkill));
                    trainingEventWriteRepository.save(event);
                    playerWriteRepository.save(player);
                } else {
                    failed.getAndSet(failed.get() + 1);
                }
            }
        );
        log.info("Finished running PotentialRiseUseCase for {} players successful {}, failed {}",
            players.size(), successful.get(), failed.get());
    }
}