package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.application.usecases.CreateMatchUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
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

    private final GetLeagueUseCase getLeagueUseCase;
    private final LeagueWriteRepository leagueWriteRepository;
    private final CreateMatchUseCase createMatchUseCase;
    private final MatchScheduler matchScheduler;

    public void schedule(List<GenerateMatchScheduleUseCase.ScheduledMatch> scheduledMatchList, String leagueId) {
        log.info("ScheduleLeagueMatchesUseCase for {} matches", scheduledMatchList.size());

        League league = getLeagueUseCase.get(leagueId);

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

        league.setScheduledMatches(true);
        leagueWriteRepository.save(league);
        log.info("Successfully scheduled {} matches.", scheduledMatchList.size());
    }
}
