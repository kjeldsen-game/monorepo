package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.Player;
import com.kjeldsen.match.domain.events.PlayStartedEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PlayStartUseCase {

    public void startPlay(ImmutablePair<Player, Player> players) {
        // TODO log which teams are playing
        PlayStartedEvent.builder()
            .eventId("adsd")
            .date(Instant.now())
            .matchId("UUID")
            .build();
    }

}
