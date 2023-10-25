package com.kjeldsen.match.engine.exceptions;

public class GameStateException extends RuntimeException {

    /*
     * Exception to be thrown when the game state is invalid and the match cannot continue.
     */

    public GameStateException(String message) {
        super(message);
    }
}
