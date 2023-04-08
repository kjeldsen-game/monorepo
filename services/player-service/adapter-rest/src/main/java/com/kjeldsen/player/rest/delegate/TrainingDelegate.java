package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.*;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GenerateTrainingUseCase generateTrainingUseCase;
    private final GetHistoricalTrainingUseCase getHistoricalTrainingUseCase;
    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase;
    private final GenerateDeclinePhaseUseCase generateDeclinePhaseUseCase;
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase;

    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> getHistoricalTraining(String playerId) {

        List<PlayerTrainingResponse> trainings = getHistoricalTrainingUseCase.get(PlayerId.of(playerId))
            .stream()
            .map(this::playerTrainingEvent2PlayerTrainingResponse)
            .toList();

        PlayerHistoricalTrainingResponse playerHistoricalTrainingResponse = new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings);
        return ResponseEntity.ok(playerHistoricalTrainingResponse);
    }

    @Override
    public ResponseEntity<Void> registerDeclinePhase(String playerId, RegisterDeclinePhaseRequest registerDeclinePhaseRequest) {

        generateDeclinePhaseUseCase.generate(
            registerDeclinePhaseRequest.getDeclineSpeed(),
            registerDeclinePhaseRequest.getDeclineStartAge(),
            PlayerId.of(playerId));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> registerBloomPhase(String playerId, RegisterBloomPhaseRequest registerBloomPhaseRequest) {

        generateBloomPhaseUseCase.generate(registerBloomPhaseRequest.getYearsOn(),
            registerBloomPhaseRequest.getBloomSpeed(),
            registerBloomPhaseRequest.getBloomStartAge(),
            PlayerId.of(playerId));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private PlayerSkill playerSkill2DomainPlayerSkill(com.kjeldsen.player.domain.PlayerSkill playerSkill) {
        return PlayerSkill.valueOf(playerSkill.name());
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .currentDay(playerTrainingEvent.getCurrentDay())
            .playerId(playerTrainingEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingEvent.getSkill()))
            .points(playerTrainingEvent.getPoints())
            .pointsBeforeTraining(playerTrainingEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingEvent.getPointsAfterTraining());
    }

    @Override
    public ResponseEntity<PlayerDeclineRightPhaseResponse> registerPlayerDeclineRightPhase(
        String playerId,
        RegisterRightDeclinePhaseRequest registerPlayerDeclineRightPhaseRequest) {

        generateSingleDeclineTrainingUseCase.generate(
            registerPlayerDeclineRightPhaseRequest.getDeclineSpeed(),
            registerPlayerDeclineRightPhaseRequest.getDeclineStartAge());

        return ResponseEntity.ok(new PlayerDeclineRightPhaseResponse());

    }

    private PlayerDeclineRightPhaseResponse playerDeclineRightPhase2PlayerDeclineRightPhaseResponse(PlayerTrainingDeclineEvent playerTrainingDeclineEvent) {
        return new PlayerDeclineRightPhaseResponse()
            .currentDay(playerTrainingDeclineEvent.getCurrentDay())
            .playerId(playerTrainingDeclineEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingDeclineEvent.getSkill()))
            .pointsToSubtract(playerTrainingDeclineEvent.getPointsToSubtract())
            .pointsBeforeTraining(playerTrainingDeclineEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingDeclineEvent.getPointsAfterTraining());
    }
}
