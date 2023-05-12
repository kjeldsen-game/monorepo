package com.kjeldsen.player.integration.kafka;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleProducerAndConsumerIT extends KafkaAbstractIT<AuthKafkaEventType, UserSignedUpEvent> {

    private final String TOPIC = "test-topic";

    @Test
    void exampleProduceAndConsume() {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
            .id(EventId.generate())
            .build();

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEventWrapper =
            KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(userSignedUpEvent)
                .build();

        sendEvent(TOPIC, kafkaEventWrapper);

        List<ConsumerRecord<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>>> events = getEvents(TOPIC);

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEvent = events.get(0).value();
        AuthKafkaEventType kafkaEventType = convert(kafkaEvent.getEventType(), AuthKafkaEventType.class);
        UserSignedUpEvent kafkaEventBody = convert(kafkaEvent.getEventBody(), UserSignedUpEvent.class);

        assertEquals(AuthKafkaEventType.USER_SIGNED_UP, kafkaEventType);
        assertEquals(userSignedUpEvent.getId(), kafkaEventBody.getId());
    }

}

