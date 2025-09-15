//package com.kjeldsen.player.application.usecases.trainings.simulator;
//
//import com.kjeldsen.domain.EventId;
//import com.kjeldsen.player.application.usecases.trainings.BaseTrainingUseCase;
//import com.kjeldsen.player.domain.Player;
//import com.kjeldsen.player.domain.PlayerSkill;
//import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
//import com.kjeldsen.player.domain.generator.DeclinePointsGenerator;
//import com.kjeldsen.player.domain.models.training.TrainingEvent;
//import com.kjeldsen.player.domain.provider.InstantProvider;
//import com.kjeldsen.player.domain.provider.PlayerProvider;
//import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
//import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
//import com.kjeldsen.player.domain.repositories.training.TrainingEventWriteRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ExecuteDeclineTrainingUseCase extends BaseTrainingUseCase {
//
//    private static final Integer MAX_YEAR_IN_DECLINE = 8;
//    private static final Integer DECLINE_AGE_TRIGGER = 27;
//    private final PlayerWriteRepository playerWriteRepository;
//
//    public void execute(Player player) {
//        if (player.getAge().getYears() < DECLINE_AGE_TRIGGER) {
//            throw new IllegalArgumentException("The age of the player must be greater or equal than 27 years.");
//        }
//
//        // Retrieve the player skill that have actual greater > 30
//        PlayerSkill skill = PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(player));
//        if (skill == null) {
//            return;
//        }
//
//        Optional<TrainingEvent> event = trainingEventReadRepository.findLatestByPlayerIdAndType(
//            player.getId(), TrainingEvent.TrainingType.DECLINE_TRAINING)
////                .findLatestByPlayerId(player.getId());
//
//        if (event.isPresent()) {
//            if (event.get().getPointsBeforeTraining() > event.get().getPointsAfterTraining()) {
//                executeDeclineAndStoreEvent(player, skill, 1);
//            } else {
//                executeDeclineAndStoreEvent(player, skill, event.get().getCurrentDay() + 1);
//            }
//        } else {
//            executeDeclineAndStoreEvent(player, skill, 1);
//        }
//    }
//
//    private void executeDeclineAndStoreEvent(Player player, PlayerSkill playerSkill, Integer currentDay) {
//        // Calculate how many years player is in decline phase, if fallOff is True always use the last year
//        int yearInDecline = getYearsInDecline(player.isFallCliff(), player.getAge().getYears());
//        int points = DeclinePointsGenerator.generateDeclinePoints(currentDay, yearInDecline);
//
//        TrainingEvent event = buildTrainingEvent(player, playerSkill, points,
//            TrainingEvent.TrainingType.DECLINE_TRAINING, player.isFallCliff());
//        event.setPointsBeforeTraining(player.getActualSkillPoints(playerSkill));
//        player.addDeclinePhase(event);
//        event = event.toBuilder()
//            .currentDay(currentDay)
//            .pointsAfterTraining(player.getActualSkillPoints(playerSkill))
//            .build();
//        trainingEventWriteRepository.save(event);
//        playerWriteRepository.save(player);
//    }
//
//    private int getYearsInDecline(boolean isFallOffCliff, int playerYears) {
//        return isFallOffCliff ? MAX_YEAR_IN_DECLINE : calculateYearInDecline(playerYears);
//    }
//
//    private int calculateYearInDecline(int playerYears) {
//        return playerYears - DECLINE_AGE_TRIGGER + 1;
//    }
//}
