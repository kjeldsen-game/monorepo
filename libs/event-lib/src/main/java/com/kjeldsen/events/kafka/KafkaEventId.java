package com.kjeldsen.events.kafka;

import java.util.UUID;

public record KafkaEventId(String value) {
    public static KafkaEventId generate() {
        return new KafkaEventId(UUID.randomUUID().toString());
    }
    public static KafkaEventId from(String value) {
        return new KafkaEventId(value);
    }
}
