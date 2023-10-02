package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class MatchEndedEvent extends Event {

    MatchId matchId;
}
