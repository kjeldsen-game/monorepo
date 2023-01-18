package com.kjeldsen.player.domain.events;

import java.util.UUID;

public record EventId(String value) {
    public static EventId generate() {
        return new EventId(UUID.randomUUID().toString());
    }

    public static EventId of(String value) {
        return new EventId(value);
    }
}
