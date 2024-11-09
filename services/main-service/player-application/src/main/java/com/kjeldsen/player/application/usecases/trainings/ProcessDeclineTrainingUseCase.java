package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
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

    private static final Integer DECLINE_AGE_TRIGGER = 28;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerTrainingDeclineEventReadRepository playerTrainingDeclineEventReadRepository;
    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;


    public void process() {
        AtomicReference<Integer> successfulDeclines = new AtomicReference<>(0);
        List<Player> players = playerReadRepository.findPlayerOverAge(DECLINE_AGE_TRIGGER);
        log.info("ProcessDeclineTrainingUseCase: found {} players", players.size());
        players.forEach(player -> {
            if (player.getAge().getYears() < DECLINE_AGE_TRIGGER) {
                throw new IllegalArgumentException("The age of the player must be greater or equal than 28 years.");
            }

            // TODO maybe change to get decline event from player as we store it here, no need for DB request
            //  PlayerTrainingDeclineEvent event = player.getDecline();

            Optional<PlayerTrainingDeclineEvent> event = playerTrainingDeclineEventReadRepository
                .findLatestByPlayerId(player.getId());
            PlayerSkill skill = PlayerProvider.randomSkillForSpecificPlayer(Optional.of(player));

            // TODO create a decline speed logic here
            if (event.isPresent()) {
                if (event.get().getPointsBeforeTraining() > event.get().getPointsAfterTraining()) {
                    // Last decline was successful set day to 1
                    generateAndStoreEvent(player, skill, 1, 1);
                    successfulDeclines.getAndSet(successfulDeclines.get() + 1);
                } else {
                    // Last decline was not successful, increase the day
                    generateAndStoreEvent(player, skill, event.get().getCurrentDay() + 1, 1);
                }
            } else {
                // No decline events about player before, create a new one
                generateAndStoreEvent(player, skill, 1, 1);
            }
        });
        log.info("Successful declines from previous day {} percentage {}", successfulDeclines, successfulDeclines.get() / (double) players.size() * 100);
    }

    private PlayerTrainingDeclineEvent generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay, Integer declineSpeed) {

        int points = PointsGenerator.generatePointsRise(currentDay);

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .declineSpeed(declineSpeed)
            .pointsToSubtract(points)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        player.addDeclinePhase(playerTrainingDeclineEvent);
        playerTrainingDeclineEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
        if (points != 0) {
            log.info("{}", playerTrainingDeclineEvent.getPointsBeforeTraining());
            log.info("{}", playerTrainingDeclineEvent.getPointsAfterTraining());
        }
        playerTrainingDeclineEvent = playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
        playerWriteRepository.save(player);

        return playerTrainingDeclineEvent;
    }
}
