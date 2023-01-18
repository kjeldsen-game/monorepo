package com.kjeldsen.player.persistence.events;

import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
public abstract class Event {

    private EventId eventId;
    private Instant eventDate;

}
