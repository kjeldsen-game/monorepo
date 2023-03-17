package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GenerateBloomPhaseUseCase;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.rest.api.TrainingBloomApiDelegate;
import com.kjeldsen.player.rest.model.RegisterBloomPhaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrainingBloomDelegate implements TrainingBloomApiDelegate {

    private final GenerateBloomPhaseUseCase generateBloomPhaseUseCase;

    @Override
    public ResponseEntity<Void> registerBloomPhase(RegisterBloomPhaseRequest registerBloomPhaseRequest) {

        generateBloomPhaseUseCase.generate(registerBloomPhaseRequest.getYearsOn(),
            registerBloomPhaseRequest.getBloomIncreasingSpeed(),
            registerBloomPhaseRequest.getBloomStartAge(),
            PlayerId.of(registerBloomPhaseRequest.getPlayerId()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
