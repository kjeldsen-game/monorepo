package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class OpportunityStartedEvent extends Event {

    private OpportunityId opportunityId;
    private MatchId matchId;

}
