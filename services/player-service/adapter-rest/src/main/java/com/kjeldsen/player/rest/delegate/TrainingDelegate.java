package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.training.GenerateBloomPhaseUseCase;
import com.kjeldsen.player.application.usecases.training.GetHistoricalTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.model.PlayerHistoricalTrainingResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import com.kjeldsen.player.rest.model.RegisterBloomPhaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GetHistoricalTrainingUseCase getHistoricalTrainingUseCase;
    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase;

    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> getHistoricalTraining(String playerId) {

        List<PlayerTrainingResponse> trainings = getHistoricalTrainingUseCase.get(Player.PlayerId.of(playerId))
            .stream()
            .map(this::playerTrainingEvent2PlayerTrainingResponse)
            .toList();

        PlayerHistoricalTrainingResponse playerHistoricalTrainingResponse = new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings);
        return ResponseEntity.ok(playerHistoricalTrainingResponse);
    }

    @Override
    public ResponseEntity<Void> registerBloomPhase(String playerId, RegisterBloomPhaseRequest registerBloomPhaseRequest) {

        generateBloomPhaseUseCase.generate(registerBloomPhaseRequest.getYearsOn(),
            registerBloomPhaseRequest.getBloomSpeed(),
            registerBloomPhaseRequest.getBloomStartAge(),
            Player.PlayerId.of(playerId));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private PlayerSkill playerSkill2DomainPlayerSkill(com.kjeldsen.player.domain.PlayerSkill playerSkill) {
        return PlayerSkill.valueOf(playerSkill.name()); // TODO MUDO CAMBIA ESTO A UN MAPPER
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .currentDay(playerTrainingEvent.getCurrentDay())
            .playerId(playerTrainingEvent.getPlayerId().toString())
            .skill(playerSkill2DomainPlayerSkill(playerTrainingEvent.getSkill()))
            .actualPoints(playerTrainingEvent.getActualPoints())
            .pointsBeforeTraining(playerTrainingEvent.getPointsBeforeTraining())
            .pointsAfterTraining(playerTrainingEvent.getPointsAfterTraining());
    }

}
