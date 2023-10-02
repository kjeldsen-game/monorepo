package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import com.kjeldsen.match.domain.id.TeamId;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Opportunity {

    OpportunityId opportunityId;
    MatchId matchId;
    TeamId teamId;
    List<Play> plays;
}
