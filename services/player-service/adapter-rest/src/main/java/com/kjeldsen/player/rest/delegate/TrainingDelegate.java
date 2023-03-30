package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GenerateBloomPhaseUseCase;
import com.kjeldsen.player.application.usecases.GenerateDeclinePhaseUseCase;
import com.kjeldsen.player.application.usecases.GenerateSingleTrainingUseCase;
import com.kjeldsen.player.application.usecases.GetHistoricalTrainingUseCase;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.model.PlayerHistoricalTrainingResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import com.kjeldsen.player.rest.model.RegisterBloomPhaseRequest;
import com.kjeldsen.player.rest.model.RegisterDeclinePhaseRequest;
import com.kjeldsen.player.rest.model.RegisterTrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GenerateSingleTrainingUseCase generateSingleTrainingUseCase;
    private final GetHistoricalTrainingUseCase getHistoricalTrainingUseCase;
    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase;
    private final GenerateDeclinePhaseUseCase generateDeclinePhaseUseCase;

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
    public ResponseEntity<Void> registerTraining(String playerId, RegisterTrainingRequest registerTrainingRequest) {
        generateSingleTrainingUseCase.generate(
            PlayerId.of(playerId),
            registerTrainingRequest.getSkills()
                .stream()
                .map(this::playerSkill2DomainPlayerSkill)
                .toList(),
            registerTrainingRequest.getDays());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> registerDeclinePhase(String playerId, RegisterDeclinePhaseRequest registerDeclinePhaseRequest) {

        generateDeclinePhaseUseCase.generate(registerDeclinePhaseRequest.getYearsOn(),
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

    private com.kjeldsen.player.domain.PlayerSkill playerSkill2DomainPlayerSkill(PlayerSkill playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill.name());
    }

    private PlayerTrainingResponse playerTrainingEvent2PlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent) {
        return new PlayerTrainingResponse()
            .points(playerTrainingEvent.getPoints());
    }

}
