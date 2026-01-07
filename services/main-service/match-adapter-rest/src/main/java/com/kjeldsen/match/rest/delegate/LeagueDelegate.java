package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.application.usecases.league.match.GenerateAllLeaguesMatchesUseCase;
import com.kjeldsen.match.application.usecases.league.match.GenerateScheduledMatchesUseCase;
import com.kjeldsen.match.application.usecases.league.team.AddTeamToLeagueUseCase;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.application.usecases.league.match.ScheduleLeagueMatchesUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.schedulers.GenericScheduler;
import com.kjeldsen.match.quartz.jobs.LeagueEndJob;
import com.kjeldsen.match.rest.api.LeagueApiDelegate;
import com.kjeldsen.match.rest.mappers.LeagueMapper;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueRequest;
import com.kjeldsen.match.rest.model.CreateOrAssignTeamToLeagueResponse;
import com.kjeldsen.match.rest.model.LeagueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeagueDelegate implements LeagueApiDelegate {

    private final GetLeagueUseCase getLeagueUseCase;
    private final GenerateScheduledMatchesUseCase generateScheduledMatchesUseCase;
    private final GenerateAllLeaguesMatchesUseCase generateAllLeaguesMatchesUseCase;
    private final ScheduleLeagueMatchesUseCase scheduleLeagueMatchesUseCase;
    private final AddTeamToLeagueUseCase addTeamToLeagueUseCase;
    private final GenericScheduler quartzGenericScheduler;

    @Override
    public ResponseEntity<LeagueResponse> getLeague(String leagueId) {
        League league = getLeagueUseCase.get(leagueId);
        LeagueResponse response = LeagueMapper.INSTANCE.leagueResponseMap(league);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> scheduleAllLeagues() {
        generateAllLeaguesMatchesUseCase.generate();
        quartzGenericScheduler.scheduleJob(LeagueEndJob.class, "leagueEndJob", "league",
            LocalDateTime.now().plusDays(19).withHour(23).withMinute(59).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant(), null);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> scheduleLeague(String leagueId) {
        scheduleLeagueMatchesUseCase.schedule(generateScheduledMatchesUseCase.generate(leagueId, false), leagueId);
        quartzGenericScheduler.scheduleJob(LeagueEndJob.class, "leagueEndJob", "league",
            LocalDateTime.now().plusDays(19).withHour(23).withMinute(59).withSecond(0)
                .atZone(ZoneId.systemDefault()).toInstant(), null);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CreateOrAssignTeamToLeagueResponse> createOrAssignTeamToLeague(CreateOrAssignTeamToLeagueRequest createOrAssignTeamToLeagueRequest) {
        String leagueId = addTeamToLeagueUseCase.add(createOrAssignTeamToLeagueRequest.getTeamId(), createOrAssignTeamToLeagueRequest.getTeamName());
        CreateOrAssignTeamToLeagueResponse response = new CreateOrAssignTeamToLeagueResponse().leagueId(leagueId);
        return ResponseEntity.ok(response);
    }
}
