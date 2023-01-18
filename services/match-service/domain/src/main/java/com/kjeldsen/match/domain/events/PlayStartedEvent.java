package com.kjeldsen.match.domain.events;

import com.kjeldsen.match.domain.PlayAction;
import com.kjeldsen.match.domain.PlayType;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class PlayStartedEvent extends BaseEvent {

    private String matchId;
    private PlayType type;
    private PlayAction action;
    private List<DuelEvent> duels;

}
