package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.OpportunityStartedEvent;
import com.kjeldsen.match.domain.id.EventId;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.OpportunityId;
import com.kjeldsen.match.domain.provider.InstantProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OpportunityStartUseCase {

    private final PlayStartUseCase playStartUseCase;

    public void start(MatchId matchId) {
        // TODO add validation if the match didn't start a play can not be started
        // TODO log which players are in the play

        final OpportunityId opportunityId = OpportunityId.generate();

        // TODO engine opportunity generate
        OpportunityStartedEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .opportunityId(opportunityId)
            .matchId(matchId)
            .build();

        // TODO engine opportunity generate
        IntStream.range(1, 10).forEach(i ->
            playStartUseCase.start(opportunityId)
        );
    }

}
