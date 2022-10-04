package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.rest.api.PlayerApiDelegate;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerDelegate implements PlayerApiDelegate {

    @Override
    public ResponseEntity<Void> createPlayer(CreatePlayerRequest createPlayerRequest) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
