package com.kjeldsen.match.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.publisher.MatchEventPublisher;
import com.kjeldsen.match.domain.repositories.MatchEventWriteRepository;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.MatchEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteMatchUseCase {

    private final GetMatchAttendanceUseCase getMatchAttendanceUseCase;
    private final TeamReadRepository teamReadRepository;
    private final MatchWriteRepository matchWriteRepository;
    private final PlayerReadRepository playerReadRepository;
    private final MatchEventPublisher matchEventPublisher;
    private final GetMatchUseCase getMatchUseCase;

    public void execute(String matchId) {
        log.info("ExecuteMatchUseCase for match {}", matchId);
        Match m = getMatchUseCase.get(matchId);

        if (m.getStatus() == Match.Status.SCHEDULED || m.getStatus() == Match.Status.ACCEPTED) {
            // Check if specific lineup is set for the match, if not assign default lineup
            if (!m.getHome().getSpecificLineup()) {
                buildTeam(TeamRole.HOME, m.getHome(), m.getHome().getId());
            }

            if (!m.getAway().getSpecificLineup()) {
                buildTeam(TeamRole.AWAY, m.getAway(), m.getAway().getId());
            }
            // save the lineup
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
                matchEventPublisher.publishMatchEvent(matchEvent);
            }
        } else {
            throw new RuntimeException("Invalid match status");
        }
    }

    private void buildTeam(TeamRole role, com.kjeldsen.match.domain.entities.Team team, String teamId) {
        Map<PlayerStatus, List<Player>> players = getDefaultLineup(Team.TeamId.of(teamId), role);
        team.setBench(players.get(PlayerStatus.BENCH));
        team.setPlayers(players.get(PlayerStatus.ACTIVE));
        TeamModifiers modifiers = getTeamModifiers(teamId);

        team.setTactic(modifiers.getTactic());
        team.setHorizontalPressure(modifiers.getHorizontalPressure());
        team.setVerticalPressure(modifiers.getVerticalPressure());
    }

    private TeamModifiers getTeamModifiers(String teamId) {
        TeamModifiers modifiers = new TeamModifiers();
        Team team = teamReadRepository.findById(Team.TeamId.of(teamId))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        com.kjeldsen.player.domain.TeamModifiers teamModifiers = team.getTeamModifiers();
        modifiers.setTactic(Tactic.valueOf(teamModifiers.getTactic().name()));
        modifiers.setVerticalPressure(VerticalPressure.valueOf(teamModifiers.getVerticalPressure().name()));
        modifiers.setHorizontalPressure(HorizontalPressure.valueOf(teamModifiers.getHorizontalPressure().name()));
        return modifiers;
    }

    private List<Player> getPlayersByStatus(List<com.kjeldsen.player.domain.Player> players, PlayerStatus status, TeamRole role) {
        return players.stream()
            .filter(player -> player.getStatus() == status)
            .map(player -> this.buildPlayer(player, role))
            .toList();
    }

    private Map<PlayerStatus, List<Player>> getDefaultLineup(Team.TeamId teamId, TeamRole role) {
        Map<PlayerStatus, List<Player>> lineup = new HashMap<>();
        List<com.kjeldsen.player.domain.Player> players = playerReadRepository.findByTeamId(teamId);

        lineup.put(PlayerStatus.ACTIVE, getPlayersByStatus(players, PlayerStatus.ACTIVE, role));
        lineup.put(PlayerStatus.BENCH, getPlayersByStatus(players, PlayerStatus.BENCH, role));
        return lineup;
    }

    private com.kjeldsen.match.domain.entities.Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return com.kjeldsen.match.domain.entities.Player.builder()
            .id(player.getId().value())
            .name(player.getName())
            .status(player.getStatus())
            .teamId(player.getTeamId().value())
            .teamRole(teamRole)
            .position(player.getPosition())
            .skills(skills)
            .playerOrder(player.getPlayerOrder())
            .build();
    }

}
