package com.kjeldsen.match.domain.exceptions;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException() {
        super("Match not found!");
    }
}
