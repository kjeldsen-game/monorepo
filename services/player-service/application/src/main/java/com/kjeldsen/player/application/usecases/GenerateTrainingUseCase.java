package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.kjeldsen.player.engine.PointsGenerator.generatePointsBloom;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateTrainingUseCase {

    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final PlayerTrainingBloomEventReadRepository playerTrainingBloomEventReadRepository;

    public PlayerTrainingEvent generate(PlayerId playerId, PlayerSkill skill, Integer currentDay) {
        log.info("Generating training");

        validateDays(currentDay);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        return generateAndStoreEvent(player, skill, currentDay);
    }

    private PlayerTrainingEvent generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        Optional<PlayerTrainingBloomEvent> playerBloomEvent = playerTrainingBloomEventReadRepository.findOneByPlayerId(player.getId());

        playerBloomEvent
            .ifPresent(bloomEvent -> handleBloomEvent(player, playerTrainingEvent, bloomEvent));

        if (playerBloomEvent.isEmpty()) {
            Integer points = PointsGenerator.generatePointsRise(currentDay);
            player.addSkillPoints(playerSkill, points);
            playerTrainingEvent.setPoints(points);
            playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
            playerWriteRepository.save(player);
        }

        return playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }

    private void handleBloomEvent(Player player, PlayerTrainingEvent playerTrainingEvent, PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        if (!player.isBloomActive(playerTrainingBloomEvent)) {
            return;
        }

        Integer points = PointsGenerator.generatePointsRise(playerTrainingEvent.getCurrentDay());
        player.addSkillPoints(playerTrainingEvent.getSkill(), points);

        Integer pointsToRise = generatePointsBloom(playerTrainingBloomEvent.getBloomSpeed(), points);
        player.addSkillPoints(playerTrainingEvent.getSkill(), pointsToRise);
        playerTrainingEvent.setBloom(playerTrainingBloomEvent);
        playerTrainingEvent.setPoints(points);
        playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerTrainingEvent.getSkill()));

    }

    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }

}
