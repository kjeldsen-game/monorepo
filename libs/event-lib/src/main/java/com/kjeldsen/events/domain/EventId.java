package com.kjeldsen.events.domain;

import java.util.UUID;

public record EventId(String value) {
    public static EventId generate() {
        return new EventId(UUID.randomUUID().toString());
    }
    public static EventId from(String value) {
        return new EventId(value);
    }
}
