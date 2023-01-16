package com.kjeldsen.match.domain.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class MatchStartedEvent extends BaseEvent {

    private String matchId;
    private List<String> teamIds;

}
