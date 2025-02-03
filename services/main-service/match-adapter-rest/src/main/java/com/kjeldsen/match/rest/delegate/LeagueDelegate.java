package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.application.usecases.league.GenerateMatchScheduleUseCase;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.application.usecases.league.ScheduleLeagueMatchesUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.rest.api.LeagueApiDelegate;
import com.kjeldsen.match.rest.mapper.LeagueMapper;
import com.kjeldsen.match.rest.model.LeagueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeagueDelegate implements LeagueApiDelegate {

    private final GetLeagueUseCase getLeagueUseCase;
    private final GenerateMatchScheduleUseCase generateMatchScheduleUseCase;
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase;

    @Override
    public ResponseEntity<LeagueResponse> getLeague(String leagueId) {
        League league = getLeagueUseCase.get(leagueId);
        LeagueResponse response = LeagueMapper.INSTANCE.leagueResponseMap(league);
        return ResponseEntity.ok(response);
    }

    // Temporary endpoint to trigger league start by REST API call
    @Override
    public ResponseEntity<Void> scheduleLeague(String leagueId) {
        List<GenerateMatchScheduleUseCase.ScheduledMatch> scheduled = generateMatchScheduleUseCase.generate(leagueId);
        scheduleLeagueMatchesUseCase.schedule(scheduled, leagueId);
        return ResponseEntity.ok().build();
    }
}
