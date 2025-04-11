package com.kjeldsen.auth.domain.exceptions;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        super("Team not found!");
    }
}
