package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.usecases.training.GenerateTrainingUseCase;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventReadRepository;
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
public class FindAndProcessScheduledPotentialUseCase {

    private final PlayerPotentialRiseScheduledEventReadRepository playerPotentialRiseScheduledEventReadRepository;
    private final GeneratePotentialRiseUseCase generatePotentialRiseUseCase;

    public List<PlayerPotentialRiseEvent> findAndProcess(LocalDate localDate) {
        List<PlayerPotentialRiseEvent> PlayerPotentialRiseEvents = new ArrayList<>();
        playerPotentialRiseScheduledEventReadRepository.findAllActiveScheduledPotentialRise(localDate)
            .forEach(scheduledRise -> {
                final AtomicInteger currentDay = new AtomicInteger(1);

                IntStream.rangeClosed(1, scheduledRise.getDaysToSimulate())
                    .forEach(day -> {
                        PlayerPotentialRiseEvent potentialRiseEvent = generatePotentialRiseUseCase.generate(scheduledRise.getPlayerId());
                        currentDay.getAndIncrement();
                        PlayerPotentialRiseEvents.add(potentialRiseEvent);
                    });
            });

        return PlayerPotentialRiseEvents;
    }

}
