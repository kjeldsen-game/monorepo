package com.kjeldsen.player.listener;

import com.kjeldsen.lib.events.LeagueEvent;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LeagueEventListener {

    private final GetTeamUseCase getTeamUseCase;
    private final TeamWriteRepository teamWriteRepository;

    @EventListener
    public void handleAuctionEndEvent(LeagueEvent leagueEvent) {
        log.info("LeagueEvent received: {}", leagueEvent);
        Team team = getTeamUseCase.get(Team.TeamId.of(leagueEvent.getTeamId()));
        team.setLeagueId(leagueEvent.getLeagueId());
        teamWriteRepository.save(team);
    }
}
