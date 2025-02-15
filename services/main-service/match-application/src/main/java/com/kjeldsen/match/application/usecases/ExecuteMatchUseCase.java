package com.kjeldsen.match.application.usecases;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.application.usecases.league.UpdateLeagueStandingsUseCase;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.publisher.MatchEventPublisher;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.MatchEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final TeamClientMatch teamClient;

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
            MatchReport report = new MatchReport(state, state.getPlays(), m.getHome(),
                m.getAway(), attendance.get("homeAttendance"), attendance.get("awayAttendance"));

            m.setMatchReport(report);
            MatchEvent matchEvent = MatchEvent.builder()
                .id(EventId.generate())
                .occurredAt(InstantProvider.now())
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
            throw new RuntimeException("Invalid match status");
        }
    }

    private void buildTeam(TeamRole role, com.kjeldsen.match.domain.entities.Team team, String teamId) {
        TeamDTO teamDTO = teamClient.getTeam(teamId, SecurityUtils.getCurrentUserToken());
        Map<PlayerStatus, List<Player>> players = getDefaultLineup(role, teamDTO.getPlayers());
        team.setBench(players.get(PlayerStatus.BENCH));
        team.setPlayers(players.get(PlayerStatus.ACTIVE));
        team.setTactic(Tactic.valueOf(teamDTO.getTeamModifiers().getTactic()));
        team.setHorizontalPressure(HorizontalPressure.valueOf(teamDTO.getTeamModifiers().getHorizontalPressure()));
        team.setVerticalPressure(VerticalPressure.valueOf(teamDTO.getTeamModifiers().getVerticalPressure()));
    }

    private List<Player> getPlayersByStatus(List<PlayerDTO> players, PlayerStatus status, TeamRole role) {
        return players.stream()
            .filter(player -> Objects.equals(player.getStatus(), status.name()))
            .map(player -> buildPlayer(player, role))
            .toList();
    }

    private Map<PlayerStatus, List<Player>> getDefaultLineup(TeamRole role, List<PlayerDTO> players) {
        Map<PlayerStatus, List<Player>> lineup = new HashMap<>();
        lineup.put(PlayerStatus.ACTIVE, getPlayersByStatus(players, PlayerStatus.ACTIVE, role));
        lineup.put(PlayerStatus.BENCH, getPlayersByStatus(players, PlayerStatus.BENCH, role));
        return lineup;
    }
}
