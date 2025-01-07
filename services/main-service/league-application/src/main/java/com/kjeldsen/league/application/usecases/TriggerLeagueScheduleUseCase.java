package com.kjeldsen.league.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.league.domain.League;
import com.kjeldsen.league.domain.ScheduleLeagueEvent;
import com.kjeldsen.league.domain.publishers.ScheduleLeagueEventPublisher;
import com.kjeldsen.league.domain.repositories.LeagueReadRepository;
import com.kjeldsen.league.domain.repositories.LeagueWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class TriggerLeagueScheduleUseCase {

    private final LeagueWriteRepository leagueWriteRepository;
    private final LeagueReadRepository leagueReadRepository;
    private final ScheduleLeagueEventPublisher scheduleLeagueEventPublisher;

    public void trigger(String leagueId) {
        log.info("TriggerLeagueScheduleUseCase for league={}", leagueId);

        League league = leagueReadRepository.findById(League.LeagueId.of(leagueId)).orElseThrow(
            () -> new RuntimeException(String.format("League not found with ID %s", leagueId))
        );
        league.setScheduledMatches(true);

        scheduleLeagueEventPublisher.publish(ScheduleLeagueEvent.builder()
            .teamsIds(league.getTeams().keySet().stream().toList())
            .leagueId(league.getId().value())
            .id(EventId.generate())
            .occurredAt(Instant.now())
            .build());
        leagueWriteRepository.save(league);
    }
}
