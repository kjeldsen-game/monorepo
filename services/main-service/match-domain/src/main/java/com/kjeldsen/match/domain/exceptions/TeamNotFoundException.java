package com.kjeldsen.match.domain.exceptions;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException() {
        super("Team not found!");
    }
}
