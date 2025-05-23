package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.application.usecases.player.GetPlayersUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutePlayerTrainingUseCase {

    private final PlayerTrainingEventWriteRepository playerTrainingEventWriteRepository;
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
        log.info("Player bloom year is set to {} {} {}", player.getBloomYear(), player.getBloomYear() != null, player.getAge().getYears());
        log.info("Test {}",  player.getBloomYear() != null && player.getBloomYear().equals(player.getAge().getYears()));

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .scheduledTrainingId(eventId)
            .playerId(player.getId())
            .teamId(player.getTeamId())
            .skill(playerSkill)
            .currentDay(currentDay)
            .pointsBeforeTraining(player.getActualSkills().get(playerSkill).getActual())
            .build();

        // Generate the number of points playerSkill should rise
        int points = PointsGenerator.generatePointsRise(currentDay);
        if (player.getBloomYear() != null && player.getBloomYear().equals(player.getAge().getYears())) {
            log.info("Generating points with bloom active player age {}", player.getAge().getYears());
            playerTrainingEvent.setBloom(true);
            points = points * 2;
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
        playerTrainingEvent.setPoints(points);
        playerTrainingEvent.setActualPoints(player.getActualSkillPoints(playerSkill));
        playerTrainingEvent.setPotentialPoints(player.getPotentialSkillPoints(playerSkill));
        playerTrainingEvent.setPointsAfterTraining(player.getActualSkillPoints(playerSkill));

        playerTrainingEventWriteRepository.save(playerTrainingEvent);
        playerWriteRepository.save(player);

    }


//    public void executeV2(Player.PlayerId playerId, PlayerSkill playerSkill, Integer currentDay, String eventId) {
//        Optional<PlayerTrainingEvent> latestPlayerTrainingEvent = playerTrainingEventReadRepository
//            .findLastByPlayerTrainingEvent(scheduledTraining.getId().value());
//
//        if (latestPlayerTrainingEvent.isPresent()) {
//            PlayerTrainingEvent trainingEvent = latestPlayerTrainingEvent.get();
//            if (trainingEvent.getPointsAfterTraining() > trainingEvent.getPointsBeforeTraining()) {
//                // Points increased, the new training is starting from 1 day
//                log.info("There was already successful training for player {} skill {}, set day to 1!", trainingEvent.getPlayerId(), trainingEvent.getSkill());
//                execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
//                    1, scheduledTraining.getId().value());
//            } else {
//                log.info("The previous training was not successful, setting day to {}", trainingEvent.getCurrentDay() + 1);
//                executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(),
//                    trainingEvent.getCurrentDay() + 1, scheduledTraining.getId().value());
//            }
//        } else {
//            log.info("Training is not present setting the current day directly to 1 {}", scheduledTraining.getSkill());
//            execute(scheduledTraining.getPlayerId(), scheduledTraining.getSkill(), 1,
//                scheduledTraining.getId().value());
//        }
//    }


}
