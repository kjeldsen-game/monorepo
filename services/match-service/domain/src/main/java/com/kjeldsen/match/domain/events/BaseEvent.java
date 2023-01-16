package com.kjeldsen.match.domain.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
public abstract class BaseEvent {

    private String eventId;
    private Instant date;

}
