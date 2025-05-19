package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.events.MatchEvent;
import com.kjeldsen.lib.model.player.PlayerClient;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.match.application.usecases.league.UpdateLeagueStandingsUseCase;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.exceptions.InvalidMatchStatusException;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.publisher.MatchEventPublisher;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.kjeldsen.match.application.usecases.UpdateMatchLineupUseCase.buildPlayer;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteMatchUseCase {

    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase;
    private final MatchWriteRepository matchWriteRepository;
    private final MatchEventPublisher matchEventPublisher;
    private final GetMatchUseCase getMatchUseCase;
    private final UpdateLeagueStandingsUseCase updateLeagueStandingsUseCase;
    private final TeamClientApi teamClientApi;

    public void execute(String matchId) {
        log.info("ExecuteMatchUseCase for match {}", matchId);
        Match m = getMatchUseCase.get(matchId);

        if (m.getStatus() == Match.Status.SCHEDULED || m.getStatus() == Match.Status.ACCEPTED) {

            if (!m.getHome().getSpecificLineup()) {
                buildTeam(TeamRole.HOME, m.getHome(), m.getHome().getId());
            }
            if (!m.getAway().getSpecificLineup()) {
                buildTeam(TeamRole.AWAY, m.getAway(), m.getAway().getId());
            }
            m.setStatus(Match.Status.PLAYED);
            GameState state = Game.play(m);

            Map<String, Integer> attendance = getMatchAttendanceUseCase.get(m.getHome().getId(), m.getAway().getId());
            MatchReport report = new MatchReport(state, state.getPlays(), m.getHome().deepCopy(),
                m.getAway().deepCopy(), attendance.get("homeAttendance"), attendance.get("awayAttendance"));

            m.setMatchReport(report);

            // Clear players duplication because it's already in matchReport
//            m.getAway().cleanPlayers();
//            m.getHome().cleanPlayers();

            com.kjeldsen.lib.events.MatchEvent matchEvent = MatchEvent.builder()
                .matchId(m.getId())
                .leagueId(m.getLeagueId())
                .homeTeamId(m.getHome().getId())
                .awayTeamId(m.getAway().getId())
                .homeScore(m.getMatchReport().getHomeScore())
                .awayScore(m.getMatchReport().getAwayScore())
                .homeAttendance(m.getMatchReport().getHomeAttendance())
                .awayAttendance(m.getMatchReport().getAwayAttendance())
                .build();

            matchWriteRepository.save(m);
            if (m.getLeagueId() != null){
                updateLeagueStandingsUseCase.update(matchEvent.getLeagueId(), matchEvent.getHomeTeamId(),
                    matchEvent.getAwayTeamId(), matchEvent.getHomeScore(), matchEvent.getAwayScore());
                matchEventPublisher.publishMatchEvent(matchEvent);
            }
        } else {
            throw new InvalidMatchStatusException();
        }
    }

    private void buildTeam(TeamRole role, com.kjeldsen.match.domain.entities.Team team, String teamId) {
        TeamClient teamDTO = teamClientApi.getTeam(teamId, null, null).get(0);
        Map<PlayerStatus, List<Player>> players = getDefaultLineup(role, teamDTO.getPlayers());
        team.setBench(players.get(PlayerStatus.BENCH));
        team.setPlayers(players.get(PlayerStatus.ACTIVE));
        team.setTactic(Tactic.valueOf(teamDTO.getTeamModifiers().getTactic()));
        team.setHorizontalPressure(HorizontalPressure.valueOf(teamDTO.getTeamModifiers().getHorizontalPressure()));
        team.setVerticalPressure(VerticalPressure.valueOf(teamDTO.getTeamModifiers().getVerticalPressure()));
    }

    private List<Player> getPlayersByStatus(List<PlayerClient> players, PlayerStatus status, TeamRole role) {
        return players.stream()
            .filter(player -> Objects.equals(player.getStatus(), status.name()))
            .map(player -> buildPlayer(player, role))
            .toList();
    }

    private Map<PlayerStatus, List<Player>> getDefaultLineup(TeamRole role, List<PlayerClient> players) {
        Map<PlayerStatus, List<Player>> lineup = new HashMap<>();
        lineup.put(PlayerStatus.ACTIVE, getPlayersByStatus(players, PlayerStatus.ACTIVE, role));
        lineup.put(PlayerStatus.BENCH, getPlayersByStatus(players, PlayerStatus.BENCH, role));
        return lineup;
    }
}
