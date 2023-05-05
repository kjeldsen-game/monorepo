package com.kjeldsen.events.kafka;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEventWrapper<T, B> {

    private KafkaEventId id;
    private Instant occurredAt;
    private T eventType;
    private B eventBody;

}
