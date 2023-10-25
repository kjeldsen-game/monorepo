package com.kjeldsen.match.application.events;

import com.kjeldsen.match.entities.Id;
import java.time.Instant;
import lombok.ToString;

@ToString
public abstract class Event {

    Id id;
    Instant date;

    public Event() {
        id = Id.generate();
        date = Instant.now();
    }
}
