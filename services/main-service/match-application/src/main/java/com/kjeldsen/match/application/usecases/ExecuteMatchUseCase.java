package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.events.MatchEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.match.application.mappers.TeamMapper;
import com.kjeldsen.match.application.usecases.league.UpdateLeagueStandingsUseCase;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.builders.MatchEndNotificationEventBuilder;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.exceptions.InvalidMatchStatusException;
import com.kjeldsen.match.domain.factories.MatchEventFactory;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteMatchUseCase {

    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final UpdateLeagueStandingsUseCase updateLeagueStandingsUseCase;

    private final TeamClientApi teamClientApi;
    private final MatchWriteRepository matchWriteRepository;
    private final GenericEventPublisher eventPublisher;

    public void execute(String matchId) {
        log.info("ExecuteMatchUseCase for match {}", matchId);
        Match m = getMatchUseCase.get(matchId);

        if (m.getStatus() == Match.Status.SCHEDULED || m.getStatus() == Match.Status.ACCEPTED) {

            buildTeam(TeamRole.HOME, m.getHome(), m.getHome().getId(), m.getHome().getSpecificLineup());
            buildTeam(TeamRole.AWAY, m.getAway(), m.getAway().getId(), m.getAway().getSpecificLineup());
            matchWriteRepository.save(m);
            GameState state = Game.play(m);
            Map<String, Integer> attendance = getMatchAttendanceUseCase.get(m.getHome().getId(), m.getAway().getId());
            MatchReport report = new MatchReport(state, state.getPlays(), m.getHome().deepCopy(),
                m.getAway().deepCopy(), attendance.get("homeAttendance"), attendance.get("awayAttendance"));

            m.setStatus(Match.Status.PLAYED);
            m.setMatchReport(report);
            MatchEvent matchEvent = MatchEventFactory.createMatchEvent(m);
            matchWriteRepository.save(m);

            if (m.getLeagueId() != null){
                updateLeagueStandingsUseCase.update(matchEvent.getLeagueId(), matchEvent.getHomeTeamId(),
                    matchEvent.getAwayTeamId(), matchEvent.getHomeScore(), matchEvent.getAwayScore());
                eventPublisher.publishEvent(matchEvent);
            }

            // Publish notification about match
            if (!Objects.equals(m.getHome().getId(), m.getAway().getId())) {
                eventPublisher.publishEvent(
                    MatchEndNotificationEventBuilder.build(List.of(m.getHome().getId(), m.getAway().getId()), m.getId())
                );
            }

        } else {
            throw new InvalidMatchStatusException();
        }
    }

    private void buildPlayers(boolean specific, Team team, List<Player> teamPlayers, TeamRole role) {

        if (!specific) {
            team.setPlayers(getPlayersByStatus(teamPlayers, PlayerStatus.ACTIVE, role));
            team.setBench(getPlayersByStatus(teamPlayers, PlayerStatus.BENCH, role));
        }

        team.getPlayers().forEach(
            player -> {
                Player optionalPlayer = teamPlayers.stream().filter(teamP -> teamP.getId().equals(player.getId()))
                    .findFirst().orElseThrow(RuntimeException::new);
                player.setSkills(optionalPlayer.getSkills()).setTeamRole(role);
            }
        );

        team.getBench().forEach(
            player -> {
                Player optionalPlayer = teamPlayers.stream().filter(teamP -> teamP.getId().equals(player.getId()))
                    .findFirst().orElseThrow(RuntimeException::new);
                player.setSkills(optionalPlayer.getSkills()).setTeamRole(role);
            }
        );
    }

    private void buildTeamModifiers(boolean specific, Team team, Team mappedTeam) {
        if (!specific) {
            team.setModifiers(mappedTeam.getModifiers());
            return;
        }
        if (team.getModifiers() == null) {
            team.setModifiers(mappedTeam.getModifiers());
        }
    }

    private void buildTeam(TeamRole role, com.kjeldsen.match.domain.entities.Team team, String teamId, boolean specific) {
        TeamResponse teamResponse = teamClientApi.getTeamById(teamId);
        Team teamMapped = TeamMapper.INSTANCE.mapTeamResponse(teamResponse);
        buildPlayers(specific, team, teamMapped.getPlayers(), role);
        buildTeamModifiers(specific, team, teamMapped);
    }

    private List<Player> getPlayersByStatus(List<Player> players, PlayerStatus status, TeamRole role) {
        return players.stream()
            .filter(player -> Objects.equals(player.getStatus(), status))
            .peek(player -> player.setTeamRole(role)).toList();
    }
}
