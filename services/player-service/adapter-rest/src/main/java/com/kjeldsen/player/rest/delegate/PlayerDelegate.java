package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.application.usecases.NewPlayer;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerTendency;
import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PlayerDelegate implements PlayerApiDelegate {

    private final CreatePlayerUseCase createPlayerUseCase;

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        NewPlayer command = NewPlayer.builder()
                .age(PlayerAge.of(createPlayerRequest.getAge()))
                .position(PlayerPosition.valueOf(createPlayerRequest.getPosition().name()))
                .playerTendency(PlayerTendency.valueOf(createPlayerRequest.getTendency().name()))
                .points(createPlayerRequest.getPoints())
                .build();
        createPlayerUseCase.create(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
