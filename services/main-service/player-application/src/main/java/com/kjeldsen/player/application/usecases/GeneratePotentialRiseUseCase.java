package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerPotentialRiseEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GeneratePotentialRiseUseCase {
    private static final Integer MIN_DAY = 1;

    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerPotentialRiseEventWriteRepository playerPotentialRiseEventWriteRepository;

    public PlayerPotentialRiseEvent generate(Player.PlayerId playerId, PlayerSkill randomSkill, Integer daysToSimulate, Integer rise, Integer currentDay) {
        log.info("Generating potential rise");

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));
        validateDays(daysToSimulate);
        return generateAndStoreEvent(player, rise, randomSkill, currentDay);
    }

    private PlayerPotentialRiseEvent generateAndStoreEvent(Player player, Integer rise, PlayerSkill randomSkill, Integer currentDay) {

        PlayerPotentialRiseEvent playerPotentialRiseEvent = PlayerPotentialRiseEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .potentialBeforeRaise(player.getPotentialSkillPoints(randomSkill))
            .pointsToRise(rise)
            .actualPoints(player.getActualSkillPoints(randomSkill))
            .skillThatRisen(randomSkill)
            .currentDay(currentDay)
            .build();

        player.addSkillsPotentialRisePoints(randomSkill, rise);

        playerPotentialRiseEvent.setPotentialAfterRaise(player.getPotentialSkillPoints(randomSkill));
        playerPotentialRiseEvent = playerPotentialRiseEventWriteRepository.save(playerPotentialRiseEvent);
        playerWriteRepository.save(player);

        return playerPotentialRiseEvent;
    }

    private void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }
}
