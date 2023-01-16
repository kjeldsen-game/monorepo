package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.Team;
import com.kjeldsen.match.domain.events.MatchStartedEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MatchStartUseCase {

    public void startMatch(List<Team> teams) {
        // TODO log which teams are playing
        MatchStartedEvent.builder()
            .eventId("adsd")
            .matchId("UUID")
            .date(Instant.now())
            .teamIds(teams.stream().map(Team::getId).toList())
            .build();
    }

}
