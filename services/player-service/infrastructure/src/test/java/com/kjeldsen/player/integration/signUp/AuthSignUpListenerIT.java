package com.kjeldsen.player.integration.signUp;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.integration.KafkaAbstractIT;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthSignUpListenerIT extends KafkaAbstractIT<AuthKafkaEventType, UserSignedUpEvent> {

    private final String TOPIC = "AuthSignup";

    @Autowired
    private TeamMongoRepository teamMongoRepository;

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

        Awaitility
            .await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertEquals(1, teamMongoRepository.findAll().size());
            });
    }

}

