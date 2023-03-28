package com.kjeldsen.player.domain.events;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@SuperBuilder
public abstract class Event {

    @Id
    private EventId eventId;
    private Instant eventDate;

}
