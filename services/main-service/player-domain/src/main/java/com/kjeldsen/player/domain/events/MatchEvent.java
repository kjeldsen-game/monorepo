package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collection = "MatchEvent")
@TypeAlias("MatchEvent")
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
