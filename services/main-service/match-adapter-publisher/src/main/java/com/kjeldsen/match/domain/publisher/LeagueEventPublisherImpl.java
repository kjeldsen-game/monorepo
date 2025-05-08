package com.kjeldsen.match.domain.publisher;

import com.kjeldsen.lib.events.LeagueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeagueEventPublisherImpl implements LeagueEventPublisher {
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public void publishLeagueEvent(LeagueEvent leagueEvent) {
        log.info("Publishing league event {}", leagueEvent);
        eventPublisher.publishEvent(leagueEvent);
    }
}
