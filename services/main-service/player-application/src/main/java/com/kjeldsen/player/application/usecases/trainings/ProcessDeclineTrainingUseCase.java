package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.DeclinePointsGenerator;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDeclineTrainingUseCase {

    private static final Integer MAX_YEAR_IN_DECLINE = 8;
    private static final Integer DECLINE_AGE_TRIGGER = 27;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerTrainingDeclineEventReadRepository playerTrainingDeclineEventReadRepository;
    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;

    /* ProcessDeclineTrainingUseCase is use case that is executed automatically by scheduler (quartz) and User/Team
    * cannot configure it etc. Use case get all player which are over DECLINE_AGE_TRIGGER, check if player
    * have any skill that have actual > 30 (decline works only on players that have at least one),
    * retrieve the last decline event. Based on previous event we refresh the day streak or increase it.
    * If player "fall off cliff" the worst model for decline is applied, if not it's calculated based on years in decline.
    * After that points are generated, if not 0 decline happened and skill actual value is subtracted.
    */

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

            Optional<PlayerTrainingDeclineEvent> event = playerTrainingDeclineEventReadRepository
                .findLatestByPlayerId(player.getId());

            if (event.isPresent()) {
                if (event.get().getPointsBeforeTraining() > event.get().getPointsAfterTraining()) {
                    // Last decline was successful set day to 1
                    successfulDeclines.getAndSet(successfulDeclines.get() + 1);
                    executeDeclineAndStoreEvent(player, skill, 1);
                } else {
                    // Last decline was not successful, increase the day
                    failedDeclines.getAndSet(successfulDeclines.get() + 1);
                    executeDeclineAndStoreEvent(player, skill, event.get().getCurrentDay() + 1);
                }
            } else {
                // No decline events about player before, create a new one
                failedDeclines.getAndSet(successfulDeclines.get() + 1);
                executeDeclineAndStoreEvent(player, skill, 1);
            }
        });
        log.info("ProcessDeclineTrainingUseCase results successful: {}, failed: {} noSkillOver30: {}", successfulDeclines, failedDeclines, withoutSkillDecline.get());
    }

    private int calculateYearInDecline(int playerYears) {
        return playerYears - DECLINE_AGE_TRIGGER + 1;
    }

    private void executeDeclineAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {
        // Calculate how many years player is in decline phase, if fallOff is True always use the last year
        int yearInDecline = player.isFallCliff() ? MAX_YEAR_IN_DECLINE : calculateYearInDecline(player.getAge().getYears());
        int points = DeclinePointsGenerator.generateDeclinePoints(currentDay, yearInDecline);

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsToSubtract(points)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        player.addDeclinePhase(playerTrainingDeclineEvent);
        playerTrainingDeclineEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));

        playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
        playerWriteRepository.save(player);
    }
}
