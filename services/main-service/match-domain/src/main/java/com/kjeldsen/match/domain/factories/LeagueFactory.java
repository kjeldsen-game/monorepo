package com.kjeldsen.match.domain.factories;

import com.kjeldsen.match.domain.entities.League;

import java.time.Instant;
import java.util.HashMap;

public class LeagueFactory {

    public static League createLeague(League.LeagueStatus status) {

        return League.builder()
            .status(status)
            .id(League.LeagueId.generate())
            .startedAt(Instant.now())
            .teams(new HashMap<>())
            .build();
    }

    public static void createLeagueStats(League league, String teamName, String teamId) {
        League.LeagueStats stats = new League.LeagueStats();
        stats.setName(teamName);
        stats.setPosition(league.getTeams().size() + 1);
        league.addTeam(teamId, stats);
    }
}
