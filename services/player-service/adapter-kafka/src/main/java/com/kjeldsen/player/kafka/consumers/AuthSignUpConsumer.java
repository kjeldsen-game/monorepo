package com.kjeldsen.player.kafka.consumers;

import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.kafka.events.UserSignedUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthSignUpConsumer {

    private final CreateTeamUseCase createTeamUseCase;

    @KafkaListener(
        topics = "${kafka.topics.auth-signup}",
        groupId = "${spring.kafka.group-id}",
        containerFactory = "signUpKafkaListenerContainerFactory")
    public void signUpKafkaConsumer(UserSignedUpEvent signup) {
        log.info("Received auth-signup event from Kafka: {}", signup);
        createTeamUseCase.create(signup.getTeamName(), 15, signup.getUserId());
    }

}
