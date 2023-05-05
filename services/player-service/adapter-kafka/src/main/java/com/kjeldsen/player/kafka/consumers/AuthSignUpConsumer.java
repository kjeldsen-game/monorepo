package com.kjeldsen.player.kafka.consumers;

import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
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
    public void signUpKafkaConsumer(KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> eventWrapper) {
        log.info("Received event from Kafka: {}", eventWrapper);
        UserSignedUpEvent userSignedUpEvent = eventWrapper.getEventBody();
        createTeamUseCase.create(userSignedUpEvent.getTeamName(), 15, userSignedUpEvent.getUserId());
    }

}
