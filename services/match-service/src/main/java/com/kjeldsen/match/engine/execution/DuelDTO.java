package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.duel.DuelStats;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DuelDTO {

    /*
     * Aggregates data about a duel for conveniently passing results and metadata around.
     */

    DuelResult result;
    DuelStats initiatorStats;
    DuelStats challengerStats;
}
