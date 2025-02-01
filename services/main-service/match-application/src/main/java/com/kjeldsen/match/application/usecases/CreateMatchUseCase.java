package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateMatchUseCase {

    private final MatchWriteRepository matchWriteRepository;
    // TODO in future communicate over events via modules
    private final TeamReadRepository teamReadRepository;

    public Match create(String homeTeamId, String awayTeamId, LocalDateTime time, String leagueId) {
        log.info("CreateMatchUseCase for homeTeamId={}, awayTeamId={} time={} leagueId={}", homeTeamId, awayTeamId, time, leagueId);
        Team.TeamId homeId = Team.TeamId.of(homeTeamId);
        com.kjeldsen.player.domain.Team home = teamReadRepository.findById(homeId)
            .orElseThrow(() -> new RuntimeException("Home team not found"));

        Team.TeamId awayId = Team.TeamId.of(awayTeamId);
        com.kjeldsen.player.domain.Team away = teamReadRepository.findById(awayId)
            .orElseThrow(() -> new RuntimeException("Away team not found"));

        com.kjeldsen.match.domain.entities.Team engineHome = buildTeam(home, TeamRole.HOME);
        com.kjeldsen.match.domain.entities.Team engineAway = buildTeam(away, TeamRole.AWAY);

        Match match = Match.builder()
            .id(java.util.UUID.randomUUID().toString())
            .home(engineHome)
            .leagueId(leagueId)
            .away(engineAway)
            .dateTime(time)
            .status(leagueId == null ? Match.Status.PENDING : Match.Status.SCHEDULED)
            .build();

        return matchWriteRepository.save(match);
    }

    private com.kjeldsen.match.domain.entities.Team buildTeam(com.kjeldsen.player.domain.Team home, TeamRole role) {
        return com.kjeldsen.match.domain.entities.Team.builder()
            .id(home.getId().value())
            .role(role)
            .specificLineup(false)
            .build();
    }
}
