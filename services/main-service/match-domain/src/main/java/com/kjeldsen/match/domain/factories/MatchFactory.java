package com.kjeldsen.match.domain.factories;

import com.kjeldsen.lib.events.MatchEvent;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;

import java.time.LocalDateTime;

public class MatchFactory {

    public static Match createMatch(Team home, Team away, String leagueId, Match.Status status, LocalDateTime time) {
        return Match.builder()
            .id(java.util.UUID.randomUUID().toString())
            .home(home)
            .leagueId(leagueId)
            .away(away)
            .dateTime(time)
            .status(status)
            .build();
    }
}
