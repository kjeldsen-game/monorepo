package com.kjeldsen.league.domain;

import com.kjeldsen.domain.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Document(collection = "ScheduleLeagueEvents")
@TypeAlias("ScheduleLeagueEvent")
public class ScheduleLeagueEvent extends Event {

    List<String> teamsIds;
    String leagueId;
}
