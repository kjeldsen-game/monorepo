package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.factories.MatchFactory;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateMatchUseCase {

    private final MatchWriteRepository matchWriteRepository;
    private final TeamClientApi teamClientApi;

    public Match create(String homeTeamId, String awayTeamId, LocalDateTime time, String leagueId) {
        log.debug("CreateMatchUseCase for homeTeamId={}, awayTeamId={} time={} leagueId={}", homeTeamId, awayTeamId, time, leagueId);

        Team engineHome = buildTeam(teamClientApi.getTeamById(homeTeamId), TeamRole.HOME);
        Team engineAway = buildTeam(teamClientApi.getTeamById(awayTeamId), TeamRole.AWAY);

        Match.Status status;
        if (Objects.equals(homeTeamId, awayTeamId)) {
            status = Match.Status.ACCEPTED;
        } else {
            status = leagueId == null ? Match.Status.PENDING : Match.Status.SCHEDULED;
        }

        return matchWriteRepository.save(MatchFactory.createMatch(engineHome, engineAway, leagueId, status, time));
    }

    private Team buildTeam(TeamResponse team, TeamRole role) {
        return Team.builder()
            .id(team.getId())
            .name(team.getName())
            .role(role)
            .specificLineup(false)
            .build();
    }
}
