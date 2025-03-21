package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.duel.DuelDisruptor;
import com.kjeldsen.match.domain.entities.duel.DuelOrigin;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.DuelStats;
import com.kjeldsen.player.domain.PitchArea;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DuelDTO {

    /*
     * Aggregates data about a duel for conveniently passing results and metadata around.
     */

    DuelResult result;
    DuelOrigin origin;
    DuelDisruptor disruptor;
    DuelParams params;
    DuelStats initiatorStats;
    DuelStats challengerStats;
    PitchArea destinationPitchArea;
}
