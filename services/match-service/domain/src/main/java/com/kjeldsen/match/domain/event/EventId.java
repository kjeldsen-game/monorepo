package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.Id;

public final class EventId extends Id<EventId> {

    public EventId(String value) {
        super(value);
    }

    public static EventId generate() {
        return from(random());
    }

    private static EventId from(String value) {
        return createOrNull(value, EventId::new);
    }

}
