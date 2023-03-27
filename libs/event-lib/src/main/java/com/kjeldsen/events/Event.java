package com.kjeldsen.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
public abstract class Event {
    private final EventId id;
    private final Instant occurredAt;
}
