package com.kjeldsen.player.application.usecases.trainings.player;

import com.kjeldsen.player.application.usecases.player.GetPlayersUseCase;
import com.kjeldsen.player.application.usecases.trainings.BaseTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutePlayerTrainingUseCase extends BaseTrainingUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final GetPlayersUseCase getPlayersUseCase;

    /* ExecutePlayerTrainingUseCase is last use case in workflow of player training use cases. It "execute" the actual
    * training mean that the points are generated based on the previous day streak (probability is calculated). Here
    * it's also considered if player is in bloom phase (if yes and points are generated -> training is successful
    * points are doubled). Exceeding the potential for skill is handled, if potential == actual increase in both happened
    * by one (Over training), if it will exceed, points are subtracted to value which can fill into.
    */

    public void execute(Player.PlayerId playerId, PlayerSkill playerSkill, Integer currentDay, String eventId) {
        log.info("ExecutePlayerTrainingUseCase for player {} skill {} on day {}", playerId, playerSkill, currentDay);

        Player player = getPlayersUseCase.get(playerId);
        TrainingEvent event = buildTrainingEvent(player, playerSkill, null, TrainingEvent.TrainingType.PLAYER_TRAINING,
            false).toBuilder().reference(eventId).pointsBeforeTraining(player.getActualSkillPoints(playerSkill))
            .currentDay(currentDay).build();

        // Generate the number of points playerSkill should rise
        int points = PointsGenerator.generatePointsRise(currentDay);
        if (player.isBloom()) {
            event.setModifier(TrainingEvent.TrainingModifier.BLOOM);
            points *= 2;
        }
        if (points > 0) {
            // Player reach the maximum potential rise up, his potential will be increase +1 as well actual skill value
            if (Objects.equals(player.getActualSkillPoints(playerSkill), player.getPotentialSkillPoints(playerSkill))) {
                log.info("Generating TrainingEvent, Training completed, maximum potential reach, updating player skill {} by 1!", playerSkill);
                points = 1;
                player.addSkillsPotentialRisePoints(playerSkill, 1);
            } else if (player.getActualSkillPoints(playerSkill) + points > player.getPotentialSkillPoints(playerSkill)) {
                // Player will exceed the potential skill value, subtract points to the max value and assign to the points
                points = player.getActualSkillPoints(playerSkill) + points - player.getPotentialSkillPoints(playerSkill);
                log.info("Generating TrainingEvent, Training completed, generated points exceeded, updating player skill {} by calculated points {}!", playerSkill, points);
            } else {
                log.info("Generating TrainingEvent, Training completed, updating player skill {} with generated points {}!", playerSkill, points);
            }
        } else {
            log.info("Generating TrainingEvent, Training completed but not successful, generating event with 0 points! for skill {}", playerSkill);
        }

        player.addSkillsPoints(playerSkill, points);
        event.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));
        event.setPoints(points);
        trainingEventWriteRepository.save(event);
        playerWriteRepository.save(player);
    }
}
