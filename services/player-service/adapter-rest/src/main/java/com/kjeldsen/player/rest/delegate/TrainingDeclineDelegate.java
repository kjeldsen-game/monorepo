package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GenerateDeclinePhaseUseCase;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.rest.api.TrainingDeclineApiDelegate;
import com.kjeldsen.player.rest.model.RegisterDeclinePhaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrainingDeclineDelegate implements TrainingDeclineApiDelegate {

    private final GenerateDeclinePhaseUseCase generateDeclinePhaseUseCase;

    @Override
    public ResponseEntity<Void> registerDeclinePhase(RegisterDeclinePhaseRequest registerDeclinePhaseRequest) {

        generateDeclinePhaseUseCase.generate(registerDeclinePhaseRequest.getYearsOn(),
            registerDeclinePhaseRequest.getDeclineSpeed(),
            registerDeclinePhaseRequest.getDeclineStartAge(),
            PlayerId.of(registerDeclinePhaseRequest.getPlayerId()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
