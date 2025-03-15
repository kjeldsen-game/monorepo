package com.kjeldsen.match.application.usecases;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
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
    private final TeamClientMatch teamClient;


    public Match create(String homeTeamId, String awayTeamId, LocalDateTime time, String leagueId) {
        log.info("CreateMatchUseCase for homeTeamId={}, awayTeamId={} time={} leagueId={}", homeTeamId, awayTeamId, time, leagueId);
        TeamDTO home = teamClient.getTeam(homeTeamId, SecurityUtils.getCurrentUserToken());
        TeamDTO away = teamClient.getTeam(awayTeamId, SecurityUtils.getCurrentUserToken());

        com.kjeldsen.match.domain.entities.Team engineHome = buildTeam(home, TeamRole.HOME);
        com.kjeldsen.match.domain.entities.Team engineAway = buildTeam(away, TeamRole.AWAY);

        Match.Status status;
        if (Objects.equals(homeTeamId, awayTeamId)) {
            System.out.println("hereeeee");
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

    private com.kjeldsen.match.domain.entities.Team buildTeam(TeamDTO team, TeamRole role) {
        return com.kjeldsen.match.domain.entities.Team.builder()
            .id(team.getId())
            .name(team.getName())
            .role(role)
            .specificLineup(false)
            .build();
    }
}
