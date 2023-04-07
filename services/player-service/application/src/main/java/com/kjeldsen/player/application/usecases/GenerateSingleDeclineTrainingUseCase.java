package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.EventId;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

import static com.kjeldsen.player.engine.PointsGenerator.generateDecreasePoints;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleDeclineTrainingUseCase {

    private static final int FIRST_DAY_OF_TRAINING = 1;
    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;

    public void generate(PlayerId playerId, Integer days) {
        log.info("Generating a decline phase");

        validateDays(days);
        PlayerSkill skill = randomSkillProvider();

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        IntStream.rangeClosed(FIRST_DAY_OF_TRAINING, days)
            .forEach(currentDay -> generateAndStoreEvent(player, skill, currentDay));
    }

    private void generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        Integer points = PointsGenerator.generatePointsRise(currentDay);
        player.subtractSkillPoints(playerSkill, generateDecreasePoints(playerTrainingDeclineEvent.getDeclineSpeed(), points));
        playerTrainingDeclineEvent.setPointsToSubtract(points);
        playerTrainingDeclineEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
        playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
    }

    private PlayerSkill randomSkillProvider() {
        PlayerSkill[] allSkills = PlayerSkill.values();
        int random = (int) (Math.random() * allSkills.length);
        return allSkills[random];
    }

    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }
}
