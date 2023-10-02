package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.OpportunityStartedEvent;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import org.springframework.stereotype.Service;

@Service
public class OpportunityStartUseCase {

    public void start(MatchId matchId) {
        validateOpportunityStart(matchId);

        OpportunityStartedEvent.builder()
            .opportunityId(OpportunityId.generate())
            .matchId(matchId)
            .build();
    }

    private void validateOpportunityStart(MatchId matchId) {
        // TODO add validation if the match didn't start a play can not be started
    }
}
