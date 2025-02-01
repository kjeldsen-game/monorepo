package com.kjeldsen.match.listener;


import com.kjeldsen.league.domain.ScheduleLeagueEvent;
import com.kjeldsen.match.application.usecases.GenerateMatchScheduleUseCase;
import com.kjeldsen.match.application.usecases.ScheduleLeagueMatchesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleLeagueEventListener {

    private final GenerateMatchScheduleUseCase generateMatchScheduleUseCase;
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase;


    @EventListener
    public void handleScheduleMatchEvent(ScheduleLeagueEvent scheduleLeagueEvent) {
        log.info("ScheduleMatchEvent received: {}", scheduleLeagueEvent);
        List<GenerateMatchScheduleUseCase.ScheduledMatch> scheduled =
            generateMatchScheduleUseCase.generate(scheduleLeagueEvent.getTeamsIds());

        scheduleLeagueMatchesUseCase.schedule(scheduled, scheduleLeagueEvent.getLeagueId());
    }
}
