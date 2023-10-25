package com.kjeldsen.match.entities.player;

public enum PlayerSkill {

    /*
     * Duel skills - these are used to determine the winners of specific duel types
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
     * Goalkeeper skills
     */

    REFLEXES,

    /*
     * General skills - these affect duel results in a more general manner
     */

    EXPERIENCE,
    AERIAL_ABILITY,
    RATING
}
