package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.aggregate.Play;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class OpportunityStartedEvent extends Event {

    OpportunityId opportunityId;
    MatchId matchId;
    List<Play> plays;
}
