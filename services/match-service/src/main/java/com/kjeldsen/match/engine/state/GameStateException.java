package com.kjeldsen.match.engine.state;

import lombok.Getter;

@Getter
public class GameStateException extends RuntimeException {

    /*
     * Exception to be thrown when the game state is invalid and the match cannot continue.
     * The last valid game state should be included if available since this can be returned to the
     * frontend so the user can inspect the place and assess why the game failed.
     */

    private final GameState state;

    public GameStateException(String message) {
        super(message);
        state = null;
    }

    public GameStateException(GameState state, String message) {
        super(message);
        this.state = state;
    }
}
