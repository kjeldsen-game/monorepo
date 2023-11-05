package com.kjeldsen.match.engine.exceptions;

import com.kjeldsen.match.engine.state.GameState;
import lombok.Getter;

@Getter
public class GameStateException extends RuntimeException {

    /*
     * Exception to be thrown when the game state is invalid and the match cannot continue.
     * The last valid game state is included in the exception.
     */

    private final GameState state;

    public GameStateException(GameState state, String message) {
        super(message);
        this.state = state;
    }
}
