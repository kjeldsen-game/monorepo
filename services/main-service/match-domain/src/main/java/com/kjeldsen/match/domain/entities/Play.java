package com.kjeldsen.match.domain.entities;

import com.kjeldsen.match.domain.entities.duel.Duel;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.match.domain.state.ChainActionSequence;
import com.kjeldsen.match.domain.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
@Data
@Setter
public class Play {

    Integer clock;
    Integer homeScore;
    Integer awayScore;
    Action action;
    Duel duel;
    BallState ballState;
    ChainActionSequence chainActionSequence;

    @Override
    public String toString() {
        // Don't show the player skills in every play. These don't change mid-game and can be
        // accessed from the team object.
        return JsonUtils.prettyPrintExclude(this, "*.skills");
    }
}
