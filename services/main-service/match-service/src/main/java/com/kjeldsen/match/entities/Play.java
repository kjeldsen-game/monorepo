package com.kjeldsen.match.entities;

import com.kjeldsen.match.entities.duel.Duel;
import com.kjeldsen.match.state.BallState;
import com.kjeldsen.match.state.ChainActionSequence;
import com.kjeldsen.match.utils.JsonUtils;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Play {

    Action action;
    Duel duel;
    Integer clock;
    BallState ballState;
    ChainActionSequence chainActionSequence;

    @Override
    public String toString() {
        // Don't show the player skills in every play. These don't change mid-game and can be
        // accessed from the team object.
        return JsonUtils.prettyPrintExclude(this, "*.skills");
    }
}
