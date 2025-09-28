package com.kjeldsen.lib.core;

import com.kjeldsen.lib.publishers.GenericEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class GenericEventPublisherImpl implements GenericEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public <T> void publishEvent(T event) {
        log.info("Publishing Event {}", event);
        eventPublisher.publishEvent(event);
    }
}
