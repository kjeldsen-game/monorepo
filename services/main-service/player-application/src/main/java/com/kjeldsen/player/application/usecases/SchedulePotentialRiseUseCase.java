package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseScheduledEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class SchedulePotentialRiseUseCase {

    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerPotentialRiseScheduledEventWriteRepository playerPotentialRiseScheduledEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void generate(Player.PlayerId playerId, Integer daysToSimulate) {
        log.info("Generating scheduled potentialRise");

        validateDays(daysToSimulate);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        generateAndStoreEvent(player, daysToSimulate);
    }

    private void generateAndStoreEvent(Player player, Integer daysToSimulate) {

        final Instant now = InstantProvider.now();

        PlayerPotentialRiseScheduledEvent playerPotentialRiseScheduledEvent = PlayerPotentialRiseScheduledEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .daysToSimulate(daysToSimulate)
            .startDate(now.truncatedTo(ChronoUnit.DAYS))
            .endDate(now.plus(daysToSimulate, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS))
            .build();

        playerPotentialRiseScheduledEventWriteRepository.save(playerPotentialRiseScheduledEvent);
    }

    private void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }
}
