package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleTrainingUseCase {

    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void generate(Player.PlayerId playerId, PlayerSkill skill, Integer trainingDays) {
        log.info("Scheduling training");

        validateDays(trainingDays);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        generateAndStoreEvent(player, skill, trainingDays);
    }

    private void generateAndStoreEvent(Player player, PlayerSkill skill, Integer trainingDays) {

        final Instant now = InstantProvider.now();

        PlayerTrainingScheduledEvent playerTrainingScheduledEvent = PlayerTrainingScheduledEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(skill)
            .trainingDays(trainingDays)
            .startDate(now.truncatedTo(ChronoUnit.DAYS))
            .endDate(now.plus(trainingDays, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS))
            .build();

        playerTrainingScheduledEventWriteRepository.save(playerTrainingScheduledEvent);
    }

    private void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }
}
