package com.kjeldsen.league.domain.publishers;

import com.kjeldsen.league.domain.ScheduleLeagueEvent;

public interface ScheduleLeagueEventPublisher {
    void publish(ScheduleLeagueEvent scheduleLeagueEvent);
}
