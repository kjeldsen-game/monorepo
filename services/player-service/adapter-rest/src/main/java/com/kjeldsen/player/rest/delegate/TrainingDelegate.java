package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.GenerateSingleTrainingUseCase;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.rest.api.TrainingApiDelegate;
import com.kjeldsen.player.rest.model.PlayerSkill;
import com.kjeldsen.player.rest.model.RegisterTrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrainingDelegate implements TrainingApiDelegate {

    private final GenerateSingleTrainingUseCase generateSingleTrainingUseCase;

    @Override
    public ResponseEntity<Void> registerTraining(RegisterTrainingRequest registerTrainingRequest) {
        generateSingleTrainingUseCase.generate(
            PlayerId.of(registerTrainingRequest.getPlayerId()),
            registerTrainingRequest.getSkills()
                .stream()
                .map(this::playerSkill2DomainPlayerSkill)
                .toList(),
            registerTrainingRequest.getDays());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private com.kjeldsen.player.domain.PlayerSkill playerSkill2DomainPlayerSkill(PlayerSkill playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill.name());
    }

}
