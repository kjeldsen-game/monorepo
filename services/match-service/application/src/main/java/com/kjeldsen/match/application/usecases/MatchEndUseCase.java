package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.MatchEndedEvent;
import com.kjeldsen.match.domain.id.EventId;
import com.kjeldsen.match.domain.id.MatchId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchEndUseCase {

    public void end(MatchId matchId) {
        log.info("Ending match {}", matchId);

        validateMatchEnd(matchId);

        MatchEndedEvent.builder()
            .matchId(matchId)
            .build();
    }

    private void validateMatchEnd(MatchId matchId) {
        // TODO - match didn't start, match has already ended etc.
    }
}
