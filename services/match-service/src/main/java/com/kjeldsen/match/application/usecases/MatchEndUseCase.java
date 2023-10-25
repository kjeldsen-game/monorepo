package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.events.MatchEndedEvent;
import com.kjeldsen.match.entities.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchEndUseCase {

    public void end(Id matchId) {
        log.info("Ending match {}", matchId);

        MatchEndedEvent.builder()
            .matchId(matchId)
            .build();
    }
}
