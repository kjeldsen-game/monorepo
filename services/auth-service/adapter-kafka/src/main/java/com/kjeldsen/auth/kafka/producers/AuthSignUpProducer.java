package com.kjeldsen.auth.kafka.producers;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.kafka.events.AuthKafkaEventType;
import com.kjeldsen.auth.kafka.events.UserSignedUpEvent;
import com.kjeldsen.events.kafka.KafkaEventId;
import com.kjeldsen.events.kafka.KafkaEventWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthSignUpProducer {

    @Value("${kafka.topics.auth-signup}")
    private String signUpTopic;

    private final KafkaTemplate<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>> authSignUpEventKafkaTemplate;

    public void send(SignUp signUp) {
        log.info("Sending signup event to Kafka: {}", signUp);

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> event = KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .id(KafkaEventId.generate())
                .occurredAt(Instant.now())
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(UserSignedUpEvent.from(signUp))
                .build();

        authSignUpEventKafkaTemplate.send(signUpTopic, event);
    }

}
