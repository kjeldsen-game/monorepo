package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.MatchEndedEvent;
import com.kjeldsen.match.domain.id.EventId;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.provider.InstantProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchEndUseCase {

    public void endMatch(MatchId matchId) {

        log.info("Ending match {}", matchId);

        // TODO add validation if the match didn't start it can not be ended

        MatchEndedEvent.builder()
            .eventId(EventId.generate())
            .date(InstantProvider.now())
            .matchId(matchId)
            .build();
    }

}
