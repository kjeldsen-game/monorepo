package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.application.usecases.GenerateTrainingUseCase;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPlayerTrainingUseCase {

    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    private final PlayerTrainingEventReadRepository playerTrainingEventReadRepository;
    private final GenerateTrainingUseCase generateTrainingUseCase;

    public void process() {
        // 1. get last playerTrainingEvent based on the PlayerTrainingScheduledTrainingId
        // 2. check current day of the latest training and compare points before/after training
        // 3. If after was higher set the current day again to 1 if not set current day to (current day + 1)
        // If the latest playerTrainingEvent was null set it right to the day 1
        List<PlayerTrainingScheduledEvent> playerTrainingScheduledEvents = playerTrainingScheduledEventReadRepository
            .findAllActiveScheduledTrainings();
        log.info("ProcessPlayerTrainingUseCase for {} trainings", playerTrainingScheduledEvents.size());
        playerTrainingScheduledEvents.forEach(scheduledTraining -> {
            Optional<PlayerTrainingEvent> latestPlayerTrainingEvent = playerTrainingEventReadRepository
                .findLastByPlayerTrainingEvent(scheduledTraining.getId().value());

            if (latestPlayerTrainingEvent.isPresent()) {
                PlayerTrainingEvent trainingEvent = latestPlayerTrainingEvent.get();
                if (trainingEvent.getPointsAfterTraining() > trainingEvent.getPointsBeforeTraining()) {
                    // Points increased, the new training is starting from 1 day
                    log.info("Training is present, there was already a training before which was successful, day set to 1");
                    generateTrainingUseCase.generate2(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
                        1, scheduledTraining.getId().value());
                } else {
                    // Increase the current day
                    log.info("Training is present, there was not a training before which was successful, setting day to {}",
                        trainingEvent.getCurrentDay() + 1);
                    generateTrainingUseCase.generate2(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
                        trainingEvent.getCurrentDay() + 1, scheduledTraining.getId().value());
                }
            } else {
                log.info("Training is not present setting the current day directly to 1 {}", scheduledTraining.getSkill());
                generateTrainingUseCase.generate2(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(), 1,
                    scheduledTraining.getId().value());
            }
        });
    }
}
