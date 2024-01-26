package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindAndProcessScheduledPotentialUseCase {
    private static final Integer MAX_AGE = 21;
    private final PlayerPotentialRiseScheduledEventReadRepository playerPotentialRiseScheduledEventReadRepository;
    private final PlayerReadRepository playerReadRepository;
    private final GeneratePotentialRiseUseCase generatePotentialRiseUseCase;

    public List<PlayerPotentialRiseEvent> findAndProcess(LocalDate localDate) {
        List<PlayerPotentialRiseEvent> PlayerPotentialRiseEvents = new ArrayList<>();
        playerPotentialRiseScheduledEventReadRepository.findAllActiveScheduledPotentialRise(localDate)
            .forEach(scheduledRise -> {
                final AtomicInteger currentDay = new AtomicInteger(1);

                Optional<Player> player = playerReadRepository.findOneById(scheduledRise.getPlayerId());
                if (player.get().getAge() >= MAX_AGE){
                    throw new IllegalArgumentException("The age of the player must be less than 21 years.");
                }
                IntStream.rangeClosed(1, scheduledRise.getDaysToSimulate())
                        .forEach(day -> {
                            Integer rise = PotentialRiseGenerator.generatePotentialRaise();
                            PlayerSkill randomSkill = PlayerProvider.randomSkillForSpecificPlayer(player);
                            if(rise!=0){
                                PlayerPotentialRiseEvent potentialRiseEvent = generatePotentialRiseUseCase.generate(
                                        scheduledRise.getPlayerId(), randomSkill, currentDay.getAndIncrement(), rise, day);
                                PlayerPotentialRiseEvents.add(potentialRiseEvent);
                            }
                        });
            });

        return PlayerPotentialRiseEvents;
    }

}
