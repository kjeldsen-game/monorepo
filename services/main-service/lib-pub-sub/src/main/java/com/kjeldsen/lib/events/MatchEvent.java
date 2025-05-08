package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class MatchEvent extends Event {

    String matchId;
    String leagueId;
    String homeTeamId;
    String awayTeamId;
    Integer homeScore;
    Integer awayScore;
    Integer homeAttendance;
    Integer awayAttendance;
}
