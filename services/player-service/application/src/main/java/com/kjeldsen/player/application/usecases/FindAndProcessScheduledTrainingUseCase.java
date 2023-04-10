package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindAndProcessScheduledTrainingUseCase {
    private final PlayerTrainingDeclineEventReadRepository playerTrainingDeclineEventReadRepository;

    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    private final GenerateTrainingUseCase generateTrainingUseCase;

    public List<PlayerTrainingEvent> findAndProcess(LocalDate localDate) {
        List<PlayerTrainingEvent> playerTrainingEvents = new ArrayList<>();
        playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings(localDate)
            .forEach(scheduledTraining -> {
                final AtomicInteger currentDay = new AtomicInteger(1);

                IntStream.rangeClosed(1, scheduledTraining.getTrainingDays())
                    .forEach(day -> {

                        PlayerTrainingEvent trainingEvent = generateTrainingUseCase.generate(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
                            currentDay.getAndIncrement());
                        playerTrainingEvents.add(trainingEvent);

                        if (trainingEvent.getPointsAfterTraining() > trainingEvent.getPointsBeforeTraining()) {
                            currentDay.set(1);
                        }
                    });
            });

        return playerTrainingEvents;
    }

}
