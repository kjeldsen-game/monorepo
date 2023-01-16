package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.events.MatchEndedEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MatchEndUseCase {

    public void endMatch(String matchId) {
        // TODO log which team is ended
        MatchEndedEvent.builder()
            .eventId("adsd")
            .date(Instant.now())
            .matchId(matchId)
            .build();
    }

}
