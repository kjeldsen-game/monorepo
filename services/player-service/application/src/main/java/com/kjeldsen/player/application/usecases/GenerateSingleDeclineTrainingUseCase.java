package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.engine.PointsGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kjeldsen.player.engine.PointsGenerator.generateDecreasePoints;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleDeclineTrainingUseCase {

    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public PlayerTrainingDeclineEvent generate(PlayerId playerId, Integer currentDay, Integer declineSpeed) {
        log.info("Generating a decline phase");

        PlayerSkill skill = randomSkillProvider();

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));

        return generateAndStoreEvent(player, skill, currentDay, declineSpeed);
    }

    private PlayerTrainingDeclineEvent generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay, Integer declineSpeed) {

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .declineSpeed(declineSpeed)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        Integer points = PointsGenerator.generatePointsRise(currentDay);
        player.subtractSkillPoints(playerSkill, generateDecreasePoints(declineSpeed, points));
        playerTrainingDeclineEvent.setPointsToSubtract(points);
        playerTrainingDeclineEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));

        playerTrainingDeclineEvent = playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
        playerWriteRepository.save(player);

        return playerTrainingDeclineEvent;
    }

    private PlayerSkill randomSkillProvider() {
        PlayerSkill[] allSkills = PlayerSkill.values();
        int random = (int) (Math.random() * allSkills.length);
        return allSkills[random];
    }

}
