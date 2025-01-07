package com.kjeldsen.league.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.league.application.usecases.TriggerLeagueScheduleUseCase;
import com.kjeldsen.league.domain.League;
import com.kjeldsen.league.domain.publishers.ScheduleLeagueEventPublisher;
import com.kjeldsen.league.domain.repositories.LeagueReadRepository;
import com.kjeldsen.league.rest.api.LeagueApiDelegate;
import com.kjeldsen.league.rest.mapper.LeagueMapper;
import com.kjeldsen.league.rest.model.LeagueResponse;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeagueDelegate implements LeagueApiDelegate {

    private final TriggerLeagueScheduleUseCase triggerLeagueScheduleUseCase;
    private final LeagueReadRepository leagueReadRepository;
    private final TeamReadRepository teamReadRepository;


    @Override
    public ResponseEntity<LeagueResponse> getLeague(String leagueId) {
        // TODO rework to the use case
        Team team = teamReadRepository.findByUserId(SecurityUtils.getCurrentUserId()).orElseThrow(
            () -> new RuntimeException("Team not found"));
        League league = leagueReadRepository.findById(League.LeagueId.of(leagueId)).orElseThrow(
            () -> new RuntimeException("League not found"));

        LeagueResponse response = LeagueMapper.INSTANCE.leagueResponseMap(league);
        return ResponseEntity.ok(response);
    }

    // Temporary endpoint to trigger league start by REST API call
    @Override
    public ResponseEntity<Void> scheduleLeague(String leagueId) {
        triggerLeagueScheduleUseCase.trigger(leagueId);
        return ResponseEntity.ok().build();
    }
}
