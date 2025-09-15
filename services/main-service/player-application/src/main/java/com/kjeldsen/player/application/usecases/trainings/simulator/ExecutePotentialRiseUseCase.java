package com.kjeldsen.player.application.usecases.trainings.simulator;

import com.kjeldsen.player.application.usecases.trainings.BaseTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutePotentialRiseUseCase extends BaseTrainingUseCase {

    private static final Integer MAX_AGE = 21;
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
            TrainingEvent event = buildTrainingEvent(player, randomSkill, rise, TrainingEvent.TrainingType.POTENTIAL_RISE, false);
            player.addSkillsPotentialRisePoints(randomSkill, rise);
            event.setPointsAfterTraining(player.getPotentialSkillPoints(randomSkill));
            trainingEventWriteRepository.save(event);
            playerWriteRepository.save(player);
        } else {
//            failed.getAndSet(successful.get() + 1);
        }
    }
}
