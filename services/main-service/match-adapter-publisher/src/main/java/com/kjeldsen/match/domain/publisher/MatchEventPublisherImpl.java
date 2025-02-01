package com.kjeldsen.match.domain.publisher;


import com.kjeldsen.player.domain.events.MatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchEventPublisherImpl implements MatchEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishMatchEvent(MatchEvent matchEvent) {
        log.info("Publishing match event {}", matchEvent);
        eventPublisher.publishEvent(matchEvent);
    }
}
