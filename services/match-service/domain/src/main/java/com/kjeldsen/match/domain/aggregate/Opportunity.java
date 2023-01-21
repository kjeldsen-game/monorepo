package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import lombok.Getter;

import java.util.List;

@Getter
public class Opportunity {

    private OpportunityId opportunityId;
    private MatchId matchId;
    private List<Play> plays;

}