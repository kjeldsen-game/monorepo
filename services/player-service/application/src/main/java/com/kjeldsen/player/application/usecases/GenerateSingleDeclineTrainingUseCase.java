package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateSingleDeclineTrainingUseCase {

    private final PlayerTrainingDeclineEventWriteRepository playerTrainingDeclineEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public PlayerTrainingDeclineEvent generate(Player.PlayerId playerId, Integer currentDay, Integer declineSpeed) {
        log.info("Generating a decline phase");

        PlayerSkill skill = PlayerProvider.randomSkill();

        Player player = playerReadRepository.findOneById(playerId)
            .orElseThrow(() -> new RuntimeException("Player not found."));

        return generateAndStoreEvent(player, skill, currentDay, declineSpeed);
    }

    private PlayerTrainingDeclineEvent generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay, Integer declineSpeed) {

        int points = PointsGenerator.generatePointsRise(currentDay);

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = PlayerTrainingDeclineEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .declineSpeed(declineSpeed)
            .pointsToSubtract(points)
            .pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .build();

        player.addDeclinePhase(playerTrainingDeclineEvent);
        playerTrainingDeclineEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));

        playerTrainingDeclineEvent = playerTrainingDeclineEventWriteRepository.save(playerTrainingDeclineEvent);
        playerWriteRepository.save(player);

        return playerTrainingDeclineEvent;
    }

}
