package com.kjeldsen.player.application.usecases.trainings.decline;

import com.kjeldsen.player.application.usecases.trainings.BaseTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.generator.DeclinePointsGenerator;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>The process works as follows:</p>
 * <ol>
 *   <li>Retrieve all players whose age is greater than or equal to {@code DECLINE_AGE_TRIGGER}.</li>
 *   <li>Filter players that have at least one skill with an actual value above 30
 *       (decline only applies if such a skill exists).</li>
 *   <li>For each eligible player, fetch the last recorded decline event from history.</li>
 *   <li>Update the decline streak:
 *       <ul>
 *         <li>If the player has already "fallen off the cliff," apply the harshest decline model.</li>
 *         <li>Otherwise, calculate decline severity based on the number of years in decline.</li>
 *       </ul>
 *   </li>
 *   <li>Generate decline points for the affected skill(s).
 *       If the result is greater than zero, apply the decline by subtracting the
 *       generated points from the player's actual skill value.</li>
 * </ol>
 *
 * <p>The outcome is a new {@code TrainingEvent} of type DECLINE_TRAINING
 *
 * <p><b>Notes:</b></p>
 * This use case is executed automatically by a scheduled Quartz job and cannot be
 * triggered or configured manually by users or teams.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDeclineTrainingUseCase extends BaseTrainingUseCase {

    private static final Integer MAX_YEAR_IN_DECLINE = 8;
    private static final Integer DECLINE_AGE_TRIGGER = 27;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public void process() {
        // Temporary variables to see values for debug purposes
        AtomicReference<Integer> withoutSkillDecline = new AtomicReference<>(0);
        AtomicReference<Integer> failedDeclines = new AtomicReference<>(0);
        AtomicReference<Integer> successfulDeclines = new AtomicReference<>(0);

        List<Player> players = playerReadRepository.findPlayerOverAge(DECLINE_AGE_TRIGGER);
        log.info("ProcessDeclineTrainingUseCase: found {} players", players.size());

        players.forEach(player -> {
            if (player.getAge().getYears() < DECLINE_AGE_TRIGGER) {
                throw new IllegalArgumentException("The age of the player must be greater or equal than 27 years.");
            }

            // Retrieve the player skill that have actual greater > 30
            PlayerSkill skill = PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(player));
            if (skill == null) {
                withoutSkillDecline.getAndSet(withoutSkillDecline.get() + 1);
                return;
            }

            Optional<TrainingEvent> ev = trainingEventReadRepository.findLatestByPlayerIdAndType(player.getId(),
                TrainingEvent.TrainingType.DECLINE_TRAINING);

            if (ev.isPresent()) {
                if (ev.get().getPointsBeforeTraining() > ev.get().getPointsAfterTraining()) {
                    // Last decline was successful set day to 1
                    successfulDeclines.getAndSet(successfulDeclines.get() + 1);
                    executeDeclineAndStoreEvent(player, skill, 1);
                } else {
                    // Last decline was not successful, increase the day
                    failedDeclines.getAndSet(failedDeclines.get() + 1);
                    executeDeclineAndStoreEvent(player, skill, ev.get().getCurrentDay() + 1);
                }
            } else {
                // No decline events about player before, create a new one
                failedDeclines.getAndSet(failedDeclines.get() + 1);
                executeDeclineAndStoreEvent(player, skill, 1);
            }
        });
        log.info("ProcessDeclineTrainingUseCase results successful: {}, failed: {} noSkillOver30: {}", successfulDeclines, failedDeclines, withoutSkillDecline.get());
    }

    private void executeDeclineAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {
        // Calculate how many years player is in decline phase, if fallOff is True always use the last year
        int yearInDecline = getYearsInDecline(player.isFallCliff(), player.getAge().getYears());
        int points = DeclinePointsGenerator.generateDeclinePoints(currentDay, yearInDecline);

        TrainingEvent event = buildTrainingEvent(player, playerSkill, points,
            TrainingEvent.TrainingType.DECLINE_TRAINING, player.isFallCliff());
        event.setPointsBeforeTraining(player.getActualSkillPoints(playerSkill));
        player.addDeclinePhase(event);
        event = event.toBuilder()
            .currentDay(currentDay)
            .pointsAfterTraining(player.getActualSkillPoints(playerSkill))
            .build();
        trainingEventWriteRepository.save(event);
        playerWriteRepository.save(player);
    }

    private int getYearsInDecline(boolean isFallOffCliff, int playerYears) {
        return isFallOffCliff ? MAX_YEAR_IN_DECLINE : calculateYearInDecline(playerYears);
    }

    private int calculateYearInDecline(int playerYears) {
        return playerYears - DECLINE_AGE_TRIGGER + 1;
    }
}
