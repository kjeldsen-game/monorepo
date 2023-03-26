package com.kjeldsen.events;

import java.time.Instant;

public abstract class Event {
    private final EventId id;
    private final Instant occurredAt;

    protected Event() {
        this.id = EventId.generate();
        this.occurredAt = Instant.now();
    }
    protected Event(EventId id, Instant occurredAt) {
        this.id = id;
        this.occurredAt = occurredAt;
    }

    public EventId getId() {
        return id;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
