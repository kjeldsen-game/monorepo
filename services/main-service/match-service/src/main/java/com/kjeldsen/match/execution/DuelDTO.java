package com.kjeldsen.match.execution;

import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.entities.duel.DuelResult;
import com.kjeldsen.match.entities.DuelStats;
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
    DuelParams params;
    DuelStats initiatorStats;
    DuelStats challengerStats;
}
