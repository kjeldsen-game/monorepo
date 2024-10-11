package com.kjeldsen.match.entities;

public enum Action {

    /*
     * Actions that a player can take.
     *
     * Note that actions are verbs written in imperative or infinitive form ("shoot" instead of
     * "shooting" or "shot") to distinguish them from duel types, which are written either as nouns
     * ("shot" duel) or adjectives ("positional" duel) or present participle ("passing" duel),
     * or whatever is necessary to avoid naming conflicts.
     */

    // MVP actions
    PASS,
    POSITION,
    TACKLE,
    SHOOT;

    // Future actions
    // FOUL,
    // CORNER_KICK,
    // FREE_KICK,
    // GOAL_KICK,
    // PENALTY_KICK;

    // Some actions require a teammate to be selected to receive the ball.
    public boolean requiresReceiver() {
        return switch (this) {
            case PASS -> true;
            case POSITION, TACKLE, SHOOT -> false;
        };
    }

    // How many minutes each type of play takes to execute - this should be configured so that there
    // are about 20 opportunities played per team per match.
    public int getDuration() {
        return switch (this) {
            case PASS, POSITION, TACKLE, SHOOT -> 1;
        };
    }
}

