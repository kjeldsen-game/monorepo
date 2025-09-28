package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
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
        log.info("CreateMatchUseCase for homeTeamId={}, awayTeamId={} time={} leagueId={}", homeTeamId, awayTeamId, time, leagueId);
        TeamClient home = teamClientApi.getTeam(homeTeamId, null, null).get(0);
        TeamClient away = teamClientApi.getTeam(awayTeamId, null, null).get(0);

        com.kjeldsen.match.domain.entities.Team engineHome = buildTeam(home, TeamRole.HOME);
        com.kjeldsen.match.domain.entities.Team engineAway = buildTeam(away, TeamRole.AWAY);

        Match.Status status;
        if (Objects.equals(homeTeamId, awayTeamId)) {
            status = Match.Status.ACCEPTED;
        } else {
            status = leagueId == null ? Match.Status.PENDING : Match.Status.SCHEDULED;
        }

        Match match = Match.builder()
            .id(java.util.UUID.randomUUID().toString())
            .home(engineHome)
            .leagueId(leagueId)
            .away(engineAway)
            .dateTime(time)
            .status(status)
            .build();
        return matchWriteRepository.save(match);
    }

    private com.kjeldsen.match.domain.entities.Team buildTeam(TeamClient team, TeamRole role) {
        return com.kjeldsen.match.domain.entities.Team.builder()
            .id(team.getId())
            .name(team.getName())
            .role(role)
            .specificLineup(false)
            .build();
    }
}
