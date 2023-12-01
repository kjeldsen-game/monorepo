package com.kjeldsen.match.engine.entities;

import com.kjeldsen.match.engine.entities.duel.DuelType;

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

    public DuelType getDuelType() {
        return switch (this) {
            case PASS -> DuelType.PASSING;
            case POSITION -> DuelType.POSITIONAL;
            // TACKLE is the ball control action: if the challenger of the preceding positional duel
            // won, then they initiate the following ball control duel, so the action is a tackle
            // and not a dribble since actions are framed from the perspective of the initiator.
            case TACKLE -> DuelType.BALL_CONTROL;
            case SHOOT -> DuelType.SHOT;
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

