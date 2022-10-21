package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerTendency;
import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import com.kjeldsen.player.usecases.PlayerCreator;
import com.kjeldsen.player.usecases.PlayerCreatorCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PlayerApiHandler implements PlayerApiDelegate {

    private final PlayerCreator playerCreator;

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        PlayerCreatorCommand command = PlayerCreatorCommand.builder()
                .age(PlayerAge.of(createPlayerRequest.getAge()))
                .position(PlayerPosition.valueOf(createPlayerRequest.getPosition().name()))
                .playerTendency(PlayerTendency.valueOf(createPlayerRequest.getTendency().name()))
                .points(createPlayerRequest.getPoints())
                .build();
        playerCreator.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
