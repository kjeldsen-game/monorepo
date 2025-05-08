package com.kjeldsen.player.publisher;

import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.player.domain.publishers.TeamCreationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamCreationEventPublisherImpl implements TeamCreationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(TeamCreationEvent teamCreationEvent) {
        log.info("Publishing TeamCreationEvent {}", teamCreationEvent);
        eventPublisher.publishEvent(teamCreationEvent);
    }
}
