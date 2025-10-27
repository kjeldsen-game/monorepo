package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LeagueStartBotTeamsCreationEvent extends Event {
    String leagueId;
    int count;
}
