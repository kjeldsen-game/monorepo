package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.rest.api.PlayerApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultPlayerDelegate implements PlayerApi {

    @Override
    ResponseEntity<Void> createPlayer() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
