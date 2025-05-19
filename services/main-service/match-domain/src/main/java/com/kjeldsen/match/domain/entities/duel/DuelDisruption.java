package com.kjeldsen.match.domain.entities.duel;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

/**
 * Represent a disruption that could happen during the execution of duel between players.
 * A disruption can be caused by various factors, specified by {@link DuelDisruption} and
 * then fields that could have new values.
 */
@Builder(toBuilder = true)
@Value
@ToString
public class DuelDisruption {
    DuelDisruptor type;

    PitchArea destinationPitchArea;

    Player challenger;
    Player receiver;

    double difference;
    double random;
}