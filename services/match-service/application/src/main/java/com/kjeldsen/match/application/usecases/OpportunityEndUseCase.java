package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.OpportunityEndedEvent;
import com.kjeldsen.match.domain.id.OpportunityId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpportunityEndUseCase {

    public void end(OpportunityId opportunityId) {

        validateOpportunityEnd(opportunityId);

        OpportunityEndedEvent.builder()
            .opportunityId(opportunityId)
            .build();
    }

    private void validateOpportunityEnd(OpportunityId opportunityId) {
        // TODO
    }
}
