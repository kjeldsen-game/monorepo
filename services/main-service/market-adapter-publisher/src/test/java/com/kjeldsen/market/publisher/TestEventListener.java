package com.kjeldsen.market.publisher;

import com.kjeldsen.lib.events.AuctionEndEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
@Slf4j
public class TestEventListener {

    private final List<com.kjeldsen.lib.events.AuctionEndEvent> events;

    public TestEventListener(List<com.kjeldsen.lib.events.AuctionEndEvent> argEvents) {
        this.events = argEvents;
    }

    @EventListener
    void onEvent(AuctionEndEvent event) {
        log.info("Event received: {}", event);
        events.add(event);
    }

    void reset() {
        events.clear();
    }
}
