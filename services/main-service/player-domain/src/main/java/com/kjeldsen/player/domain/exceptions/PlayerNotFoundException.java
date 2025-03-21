package com.kjeldsen.player.domain.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException() {
        super("Player not found!");
    }
}
