package com.kjeldsen.match.domain.events;

import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class PlayStartedEvent extends BaseEvent {

    private String matchId;
    private List<DuelEvent> duels;

}
