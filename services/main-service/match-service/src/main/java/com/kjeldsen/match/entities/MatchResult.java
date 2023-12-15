package com.kjeldsen.match.entities;

public enum MatchResult {

    WIN,
    DRAW,
    LOSS;

    // Result conversion to a number is used for qualifying results and calculating rating changes
    public double score() {
        return switch (this) {
            case WIN -> 1.0;
            case DRAW -> 0.5;
            case LOSS -> 0.0;
        };
    }
}
