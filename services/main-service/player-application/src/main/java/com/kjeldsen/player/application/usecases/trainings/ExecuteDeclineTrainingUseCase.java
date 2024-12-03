package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.DeclinePointsGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteDeclineTrainingUseCase {

    private static final Integer MAX_YEAR_IN_DECLINE = 8;
    private static final Integer DECLINE_AGE_TRIGGER = 27;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerTrainingDeclineEventReadRepository playerTrainingDeclineEventReadRepository;
    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;

    public void execute(Player player) {
        if (player.getAge().getYears() < DECLINE_AGE_TRIGGER) {
            throw new IllegalArgumentException("The age of the player must be greater or equal than 27 years.");
        }

        // Retrieve the player skill that have actual greater > 30
        PlayerSkill skill = PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(player));
        if (skill == null) {
            return;
        }

        Optional<PlayerTrainingDeclineEvent> event = playerTrainingDeclineEventReadRepository
                .findLatestByPlayerId(player.getId());

        if (event.isPresent()) {
            if (event.get().getPointsBeforeTraining() > event.get().getPointsAfterTraining()) {
                executeDeclineAndStoreEvent(player, skill, 1);
            } else {
                executeDeclineAndStoreEvent(player, skill, event.get().getCurrentDay() + 1);
            }
        } else {
            executeDeclineAndStoreEvent(player, skill, 1);
        }
    }

    private int calculateYearInDecline(int playerYears) {
        return playerYears - DECLINE_AGE_TRIGGER + 1;
    }

    private void executeDeclineAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {
        // Calculate how many years player is in decline phase, if fallOff is True
        // always use the last year
        int yearInDecline = player.isFallCliff() ? MAX_YEAR_IN_DECLINE
                : calculateYearInDecline(player.getAge().getYears());
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
