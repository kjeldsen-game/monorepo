package com.kjeldsen.player.integration.kafka;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthSignUpListenerIT extends KafkaAbstractIT<AuthKafkaEventType, UserSignedUpEvent> {

    private final String TOPIC = "AuthSignup";

    @Test
    void shouldConsumeUserSignedUpEvent() {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
            .id(EventId.generate())
            .build();

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEventWrapper =
            KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(userSignedUpEvent)
                .build();

        assertDoesNotThrow(() -> sendEvent(TOPIC, kafkaEventWrapper));
    }

/*    @Test
    void shouldTeamsIsSavingInDatabase() {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
            .id(EventId.generate())
            .build();

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEventWrapper =
            KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(userSignedUpEvent)
                .build();

        assertDoesNotThrow(() -> sendEvent(TOPIC, kafkaEventWrapper));
    }*/

/*    @Test
    void shouldPlayerIsSavingInDatabase() {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
            .id(EventId.generate())
            .build();

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEventWrapper =
            KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(userSignedUpEvent)
                .build();

        assertDoesNotThrow(() -> sendEvent(TOPIC, kafkaEventWrapper));
    }*/
}

