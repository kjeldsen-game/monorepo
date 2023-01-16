package com.kjeldsen.match.domain.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MatchEndedEvent extends BaseEvent {

    private String matchId;

}
