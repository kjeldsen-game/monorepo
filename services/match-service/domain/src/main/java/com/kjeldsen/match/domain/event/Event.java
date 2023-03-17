package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.EventId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
public abstract class Event {

    private EventId eventId;
    private Instant eventDate;

}
