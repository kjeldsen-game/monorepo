package com.kjeldsen.match.domain.entities.duel;

/**
 * The disruptor of a duel alters the normal flow of a play, a goalkeeper interception avoids the ball
 * to reach the receiver. Other possible disrupts could be an injury or any external event to the
 * normal flow of a duel.
 */
public enum DuelDisruptor {

    NONE,
    GOALKEEPER_INTERCEPTION,
    MISSED_PASS,
    MISSED_SHOT,
    GOALKEEPER_FUMBLE,
}
