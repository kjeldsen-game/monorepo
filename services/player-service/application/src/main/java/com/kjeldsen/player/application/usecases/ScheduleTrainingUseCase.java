package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleTrainingUseCase {

    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void generate(PlayerId playerId, Set<PlayerSkill> skills, Integer trainingDays) {
        log.info("Generating training");

        validateDays(trainingDays);
        validateSkills(skills);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        generateAndStoreEvent(player, skills, trainingDays);
    }

    private void generateAndStoreEvent(Player player, Set<PlayerSkill> skills, Integer trainingDays) {

        final Instant now = InstantProvider.now();

        PlayerTrainingScheduledEvent playerTrainingScheduledEvent = PlayerTrainingScheduledEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(player.getId())
            .skills(skills)
            .trainingDays(trainingDays)
            .startDate(now.truncatedTo(ChronoUnit.DAYS))
            .endDate(now.plus(trainingDays, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS))
            .build();

        playerTrainingScheduledEventWriteRepository.save(playerTrainingScheduledEvent);
    }

    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }

    public void validateSkills(Set<PlayerSkill> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            throw new IllegalArgumentException("Skills cannot be null or empty");
        }
    }

}
