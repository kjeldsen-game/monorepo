package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LeagueEvent extends Event {
    String leagueId;
    String teamId;
}
