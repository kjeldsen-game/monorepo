package com.kjeldsen.match.engine.entities;


public enum SkillType {

    /*
     * Player skill types
     */

    // Passing duel
    PASSING,
    INTERCEPTING,
    // Positional duel
    OFFENSIVE_POSITIONING,
    DEFENSIVE_POSITIONING,
    // Shot duel (the challenger here is the goalkeeper, with a different skill set, given below)
    SCORING,
    // Ball control duel
    BALL_CONTROL,
    TACKLING,

    /*
     * Goalkeeper skill types
     */

    REFLEXES,

    /*
     * General skills - these affect duel results in a more general manner
     */

    EXPERIENCE,
    AERIAL_ABILITY,
    RATING
}
