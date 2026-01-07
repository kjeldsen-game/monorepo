package com.kjeldsen.match.domain.factories;

import com.kjeldsen.lib.events.MatchEvent;
import com.kjeldsen.match.domain.entities.Match;

public class MatchEventFactory {

    public static MatchEvent createMatchEvent(Match match) {
       return MatchEvent.builder()
            .matchId(match.getId())
            .leagueId(match.getLeagueId())
            .homeTeamId(match.getHome().getId())
            .awayTeamId(match.getAway().getId())
            .homeScore(match.getMatchReport().getHomeScore())
            .awayScore(match.getMatchReport().getAwayScore())
            .homeAttendance(match.getMatchReport().getHomeAttendance())
            .awayAttendance(match.getMatchReport().getAwayAttendance())
            .build();
    }
}
