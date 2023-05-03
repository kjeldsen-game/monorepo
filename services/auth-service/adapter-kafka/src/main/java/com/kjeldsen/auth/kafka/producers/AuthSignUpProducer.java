package com.kjeldsen.auth.kafka.producers;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.kafka.events.UserSignedUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthSignUpProducer {

    @Value("${kafka.topics.auth-signup}")
    private String signUpTopic;

    private final KafkaTemplate<String, UserSignedUpEvent> authSignUpEventKafkaTemplate;

    public void send(SignUp signUp) {
        UserSignedUpEvent event = UserSignedUpEvent.from(signUp);
        log.info("Sending signup event to Kafka: {}", event);
        authSignUpEventKafkaTemplate.send(signUpTopic, event);
    }

}
