package com.kjeldsen.auth.publisher;

import com.kjeldsen.auth.domain.publishers.UserRegisterPublisher;
import com.kjeldsen.player.domain.events.UserRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisterPublisherImpl implements UserRegisterPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishUserRegisterEvent(UserRegisterEvent userRegisterEvent) {
        log.info("Publishing UserRegisterEvent {}", userRegisterEvent);
        eventPublisher.publishEvent(userRegisterEvent);
    }
}
