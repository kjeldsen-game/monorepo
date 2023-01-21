package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MatchStartedEvent extends Event {

    private MatchId matchId;

}
