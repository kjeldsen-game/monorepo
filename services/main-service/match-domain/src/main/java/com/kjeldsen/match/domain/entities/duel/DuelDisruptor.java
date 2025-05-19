package com.kjeldsen.match.domain.entities.duel;

public enum DuelDisruptor {

    /*
     * The disruptor of a duel alters the normal flow of a play, a goalkeeper interception avoids the ball
     * to reach the receiver. Other possible disruptors could be an injury or any external event to the
     * normal flow of a duel.
     */
    NONE,
    GOALKEEPER_INTERCEPTION,
    MISSED_PASS,
    MISSED_SHOT,
}
