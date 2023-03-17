package com.kjeldsen.auth.kafka.producers;

import com.kjeldsen.auth.domain.SignUp;
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

    private final KafkaTemplate<String, SignUp> authSignUpEventKafkaTemplate;

    public void send(SignUp signUp) {
        log.info("Sending auth-signup event to Kafka: {}", signUp);
        authSignUpEventKafkaTemplate.send(signUpTopic, signUp);
    }

}
