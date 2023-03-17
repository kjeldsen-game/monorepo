package com.kjeldsen.player.kafka.consumers;

import com.kjeldsen.player.kafka.events.SignUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthSignUpConsumer {

    @KafkaListener(
        topics = "${kafka.topics.auth-signup}",
        groupId = "${spring.kafka.group-id}",
        containerFactory = "signUpKafkaListenerContainerFactory")
    public void signUpKafkaConsumer(SignUp signup) {
        log.info("Received auth-signup event from Kafka: {}", signup);
        // TODO Miguel call your use case here. You need to add more info in the signup event if needed
    }

}
