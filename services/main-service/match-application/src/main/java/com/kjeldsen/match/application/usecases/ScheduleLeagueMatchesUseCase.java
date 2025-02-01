package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.schedulers.MatchScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleLeagueMatchesUseCase {

    private final CreateMatchUseCase createMatchUseCase;
    private final MatchScheduler matchScheduler;

    public void schedule(List<GenerateMatchScheduleUseCase.ScheduledMatch> scheduledMatchList, String leagueId) {
        log.info("ScheduleLeagueMatchesUseCase for {} matches", scheduledMatchList.size());

        for (GenerateMatchScheduleUseCase.ScheduledMatch scheduledMatch : scheduledMatchList) {
            Match match = createMatchUseCase.create(
                scheduledMatch.homeTeamId(),
                scheduledMatch.awayTeamId(),
                scheduledMatch.date(),
                leagueId);
            LocalDateTime dateTime = match.getDateTime();
            Instant instant = dateTime.toInstant(ZoneOffset.UTC);

            matchScheduler.scheduleMatch(match.getId(), instant);
        }
        log.info("Successfully scheduled {} matches.", scheduledMatchList.size());
    }
}
