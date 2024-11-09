package com.kjeldsen.player.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestEventListener<T> {

    @EventListener
    void onEvent(T event) {
        log.info("Event received: {}", event);
    }
}
