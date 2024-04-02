package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.generator.OvertrainingGenerator;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;

import static com.kjeldsen.player.domain.generator.PointsGenerator.generatePointsBloom;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenerateTrainingUseCase {
    private final PlayerTrainingEventMongoRepository playerTrainingEventMongoRepository;

    private static final Integer MIN_DAY = 1;
    private static final Integer MAX_DAY = 1000;
    private static final Range<Integer> RANGE_OF_DAYS = Range.between(MIN_DAY, MAX_DAY);

    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public PlayerTrainingEvent generate(Player.PlayerId playerId, PlayerSkill skill, Integer currentDay) {
        log.info("Generating training");

        validateDays(currentDay);

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(() -> new RuntimeException("Player not found."));
        playerAging(player);
        return generateAndStoreEvent(player, skill, currentDay);
    }

    private PlayerTrainingEvent generateAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .playerId(player.getId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsBeforeTraining(player.getActualSkills().get(playerSkill).getActual())
            .build();

        if (player.isBloomActive()) {
            handleBloomEvent(player, playerTrainingEvent);
        } else if (player.getActualSkillPoints(playerSkill).equals(player.getPotentialSkillPoints(playerSkill))) {
            handleOvertraining(player, playerTrainingEvent, currentDay, playerSkill);
        } else {
            Integer points = PointsGenerator.generatePointsRise(currentDay);
            playerTrainingEvent.setPoints(points);

            player.addSkillsPoints(playerSkill, points);
            playerTrainingEvent.setActualPoints(player.getActualSkillPoints(playerSkill));
            playerTrainingEvent.setPotentialPoints(player.getPotentialSkillPoints(playerSkill));
            playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
        }

        playerTrainingEvent = playerTrainingEventWriteRepository.save(playerTrainingEvent);
        playerWriteRepository.save(player);

        return playerTrainingEvent;
    }

    private void handleBloomEvent(Player player, PlayerTrainingEvent playerTrainingEvent) {
        Integer points = PointsGenerator.generatePointsRise(playerTrainingEvent.getCurrentDay());
        player.addSkillsPoints(playerTrainingEvent.getSkill(), points);
        Integer pointsToRise = generatePointsBloom(player.getBloom().getBloomSpeed(), points);
        player.addSkillsPoints(playerTrainingEvent.getSkill(), pointsToRise);
        playerTrainingEvent.setBloom(player.getBloom());
        playerTrainingEvent.setActualPoints(points);
        playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerTrainingEvent.getSkill()));
    }
    private void handleOvertraining(Player player, PlayerTrainingEvent playerTrainingEvent, Integer currentDay, PlayerSkill playerSkill){
        Integer points = OvertrainingGenerator.generateOvertrainingRaise(currentDay);
        playerTrainingEvent.setPoints(points);
        playerTrainingEvent.setActualPoints(player.getActualSkillPoints(playerSkill));
        player.addSkillsPoints(playerSkill, points);
        Player.checkDifferenceOvertrainingPoints(playerSkill, player);
        playerTrainingEvent.setIsOvertrainingActive(true);
        playerTrainingEvent.setPotentialPoints(player.getPotentialSkillPoints(playerSkill));
        playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
    }

    // TODO make private and refactor tests
    public void validateDays(Integer days) {
        if (!RANGE_OF_DAYS.contains(days)) {
            throw new IllegalArgumentException("Days must be between 1 and 1000");
        }
    }
    public Player playerAging(Player player){
        PlayerAge age = player.getAge();
        PlayerAge playerAge = PlayerAge.gettingOlder(age);
        player.setAge(playerAge);
        return player;
    }
}
