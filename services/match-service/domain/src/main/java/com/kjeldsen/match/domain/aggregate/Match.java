package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.event.MatchStartedEvent;
import com.kjeldsen.match.domain.id.MatchId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Match {

    MatchId matchId;
    Team home;
    Team away;

    public static Match generate(MatchStartedEvent matchStartedEvent) {
        // TODO - return the aggregated match details
        // This object need to contain all of the information required for the game engine
        return Match.builder().build();
    }
}
