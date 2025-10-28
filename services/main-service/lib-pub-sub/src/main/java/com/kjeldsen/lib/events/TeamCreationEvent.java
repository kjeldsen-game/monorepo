package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
public class TeamCreationEvent extends Event {
    String teamId;
    Map<String, String> teams;
    String leagueId;
    boolean isBots;
}
