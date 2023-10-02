package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class OpportunityEndedEvent extends Event {

    OpportunityId opportunityId;
    MatchId matchId;
}
