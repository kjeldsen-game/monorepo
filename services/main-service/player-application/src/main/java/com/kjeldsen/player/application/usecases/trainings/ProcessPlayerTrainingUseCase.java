package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
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
    private final PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    private final PlayerTrainingEventReadRepository playerTrainingEventReadRepository;
    private final ExecutePlayerTrainingUseCase executePlayerTrainingUseCase;

    /*
    * ProcessPlayerTrainingUseCase is use case that handle the processing of all player trainings that are scheduled.
    * Use case retrieve all "ACTIVE" scheduled trainings. In iteration the last executed training (these are handled by
    * ExecutePlayerTrainingUseCase) is retrieved. We compare the before/after points of event and get the day in which that
    * happened. Based on logic if training was successful day streak is refreshed to 1, if not streak is increased.
    * If no training happened yet for scheduled skill, day streak is set to 1.
    */

    public void process() {
        List<PlayerTrainingScheduledEvent> playerTrainingScheduledEvents = playerTrainingScheduledEventReadRepository
            .findAllActiveScheduledTrainings();
        log.info("ProcessPlayerTrainingUseCase for {} trainings", playerTrainingScheduledEvents.size());
        playerTrainingScheduledEvents.forEach(scheduledTraining -> {
            Optional<PlayerTrainingEvent> latestPlayerTrainingEvent = playerTrainingEventReadRepository
                .findLastByPlayerTrainingEvent(scheduledTraining.getId().value());

            if (latestPlayerTrainingEvent.isPresent()) {
                PlayerTrainingEvent trainingEvent = latestPlayerTrainingEvent.get();

                // You reach the maximum so we this training is going to be set to INACTIVE
                if (trainingEvent.getActualPoints() == 100) {
                    scheduledTraining.setStatus(PlayerTrainingScheduledEvent.Status.INACTIVE);
                    playerTrainingScheduledEventWriteRepository.save(scheduledTraining);
                    return;
                }

                if (trainingEvent.getPointsAfterTraining() > trainingEvent.getPointsBeforeTraining()) {
                    // Points increased, the new training is starting from 1 day
                    log.info("There was already successful training for player {} skill {}, set day to 1!", trainingEvent.getPlayerId(), trainingEvent.getSkill());
                    executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
                        1, scheduledTraining.getId().value());
                } else {
                    log.info("The previous training was not successful, setting day to {}", trainingEvent.getCurrentDay() + 1);
                    executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
                        trainingEvent.getCurrentDay() + 1, scheduledTraining.getId().value());
                }
            } else {
                log.info("Training is not present setting the current day directly to 1 {}", scheduledTraining.getSkill());
                executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(), 1,
                    scheduledTraining.getId().value());
            }
        });
    }
}
