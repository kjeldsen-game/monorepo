package com.kjeldsen.match.entities;

import com.kjeldsen.match.application.events.MatchStartedEvent;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Match {

    Id id;
    Team home;
    Team away;

    public static Match generate(MatchStartedEvent matchStartedEvent) {
        return Match.builder()
            .build();
    }
}
