package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.event.EventId;
import com.kjeldsen.match.domain.event.MatchStartedEvent;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.provider.InstantProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchStartUseCase {

    public void startMatch(MatchId matchId) {

        log.info("Starting match {}", matchId);

        // TODO add validation if the match does not exists it can not be started

        MatchStartedEvent.builder()
            .eventId(EventId.generate())
            .date(InstantProvider.now())
            .matchId(MatchId.generate())
            .build();


        // TODO after saving the match started - call engine to generate plays
    }

}
