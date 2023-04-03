package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingBloomEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleTrainingUseCase {

    private static final int FIRST_DAY_OF_TRAINING = 1;
    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerTrainingBloomEventReadRepository playerTrainingBloomEventReadRepository;
    private final PlayerTrainingDeclineEventReadRepository playerTrainingDeclineEventReadRepository;

    public void generate(PlayerId playerId, List<PlayerSkill> skills, Integer days) {
        log.info("Generating training");

        validateDays(days);
        validateSkills(skills);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        IntStream.rangeClosed(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> skills.forEach(skill -> generateAndStoreEvent(player, skill, currentDay)));
    }

    private void generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        Optional<PlayerTrainingBloomEvent> playerBloomEvent = playerTrainingBloomEventReadRepository.findOneByPlayerId(player.getId());

        playerBloomEvent
            .ifPresent(bloomEvent -> handleBloomEvent(player, playerTrainingEvent, bloomEvent));

        Optional<PlayerTrainingDeclineEvent> playerDeclineEvent = playerTrainingDeclineEventReadRepository.findOneByPlayerId(player.getId());

        playerDeclineEvent
            .ifPresent(declineEvent -> handleDeclineEvent(player, playerTrainingEvent, declineEvent));

        if (playerBloomEvent.isEmpty() && playerDeclineEvent.isEmpty()) {
            Integer points = PointsGenerator.generatePointsRise(currentDay);
            player.addSkillPoints(playerSkill, points);
            playerTrainingEvent.setPoints(points);
        }

        playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
        playerTrainingEventWriteRepository.save(playerTrainingEvent);
    }

    private void handleBloomEvent(Player player, PlayerTrainingEvent playerTrainingEvent, PlayerTrainingBloomEvent playerTrainingBloomEvent) {
        if (!player.isBloomActive(playerTrainingBloomEvent)) {
            return;
        }
        Integer points = PointsGenerator.generatePoints(playerTrainingBloomEvent.getBloomSpeed(),
            PointsGenerator.generatePointsRise(playerTrainingEvent.getCurrentDay()));
        player.addSkillPoints(playerTrainingEvent.getSkill(), points);
        playerTrainingEvent.setBloom(playerTrainingBloomEvent);
        playerTrainingEvent.setPoints(player.getActualSkillPoints(playerTrainingEvent.getSkill()));
    }

    private void handleDeclineEvent(Player player, PlayerTrainingEvent playerTrainingEvent, PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        if (!player.isDeclineActive(playerTrainingDeclineEvent)) {
            return;
        }
        Integer points = PointsGenerator.generatePoints(playerTrainingDeclineEvent.getDeclineSpeed(),
            PointsGenerator.generatePointsRise(playerTrainingEvent.getCurrentDay()));
        player.subtractSkillPoints(playerTrainingEvent.getSkill(), points);
        playerTrainingEvent.setDecline(playerTrainingDeclineEvent);
        playerTrainingEvent.setPoints(player.getActualSkillPoints(playerTrainingEvent.getSkill()));
    }

    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }

    public void validateSkills(List<PlayerSkill> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            throw new IllegalArgumentException("Skills cannot be null or empty");
        }
    }

}
