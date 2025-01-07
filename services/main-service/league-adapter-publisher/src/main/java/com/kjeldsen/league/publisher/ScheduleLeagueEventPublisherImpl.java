package com.kjeldsen.league.publisher;


import com.kjeldsen.league.domain.ScheduleLeagueEvent;
import com.kjeldsen.league.domain.publishers.ScheduleLeagueEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleLeagueEventPublisherImpl implements ScheduleLeagueEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(ScheduleLeagueEvent scheduleLeagueEvent) {
        log.info("Publishing ScheduleLeagueEvent {}", scheduleLeagueEvent);
        eventPublisher.publishEvent(scheduleLeagueEvent);
    }
}
