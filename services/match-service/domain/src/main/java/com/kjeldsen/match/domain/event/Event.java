package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.EventId;
import java.time.Instant;
import lombok.ToString;

@ToString
public abstract class Event {

    EventId eventId;
    Instant eventDate;

    public Event() {
        eventId = EventId.generate();
        eventDate = Instant.now();
    }
}
