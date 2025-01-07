package com.kjeldsen.match.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.Game;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.TeamModifiers;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.publisher.MatchEventPublisher;
import com.kjeldsen.match.repositories.MatchEventWriteRepository;
import com.kjeldsen.match.repositories.MatchReadRepository;
import com.kjeldsen.match.repositories.MatchWriteRepository;
import com.kjeldsen.match.state.GameState;
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

    private final MatchWriteRepository matchWriteRepository;
    private final MatchReadRepository matchReadRepository;
    private final TeamReadRepository teamReadRepository;
    private final PlayerReadRepository playerReadRepository;
    private final MatchEventPublisher matchEventPublisher;
    private final MatchEventWriteRepository matchEventWriteRepository;

    public void execute(String matchId) {
        log.info("ExecuteMatchUseCase for match {}", matchId);

        Optional<Match> match = Optional.ofNullable(matchReadRepository.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found")));

        // TODO modify as well for the accepted matches
        match.ifPresent(m -> {
            if (m.getStatus() == Match.Status.SCHEDULED) {
                // Check if specific lineup is set for the match, if not assign default lineup
                if (!m.getHome().getSpecificLineup()) {
                    Map<PlayerStatus, List<Player>> players = getDefaultLineup(Team.TeamId.of(m.getHome().getId()), TeamRole.HOME);
                    m.getHome().setBench(players.get(PlayerStatus.BENCH));
                    m.getHome().setPlayers(players.get(PlayerStatus.ACTIVE));
                    TeamModifiers modifiers = getTeamModifiers(m.getHome().getId());
                    m.getHome().setTactic(modifiers.getTactic());
                    m.getHome().setHorizontalPressure(modifiers.getHorizontalPressure());
                    m.getHome().setVerticalPressure(modifiers.getVerticalPressure());
                }

                if (!m.getAway().getSpecificLineup()) {
                    Map<PlayerStatus, List<Player>> players = getDefaultLineup(Team.TeamId.of(m.getAway().getId()), TeamRole.AWAY);
                    m.getAway().setBench(players.get(PlayerStatus.BENCH));
                    m.getAway().setPlayers(players.get(PlayerStatus.ACTIVE));
                    TeamModifiers modifiers = getTeamModifiers(m.getAway().getId());
                    m.getAway().setTactic(modifiers.getTactic());
                    m.getAway().setHorizontalPressure(modifiers.getHorizontalPressure());
                    m.getAway().setVerticalPressure(modifiers.getVerticalPressure());
                }
                // save the lineup
                m.setStatus(Match.Status.PLAYED);

                GameState state = Game.play(m);
                Map<String, Integer> attendance = getMatchAttendance(m);
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

                matchEventWriteRepository.save(matchEvent);
                matchEventPublisher.publishMatchEvent(matchEvent);

            }
            matchWriteRepository.save(m);
        });
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

    private com.kjeldsen.match.entities.Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return com.kjeldsen.match.entities.Player.builder()
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

    private Map<String, Integer> getMatchAttendance(Match match) {
        com.kjeldsen.player.domain.Team homeTeam = teamReadRepository.findById(Team.TeamId.of(match.getHome().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);

        com.kjeldsen.player.domain.Team awayTeam = teamReadRepository.findById(Team.TeamId.of(match.getAway().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer awayAttendance = awayTeam.getFans().getTotalFans();

        if (homeAttendance + awayAttendance > capacity) {
            float scaleFactor = (float) (homeAttendance + awayAttendance) / capacity;
            homeAttendance = Math.round(homeAttendance * scaleFactor);
            awayAttendance = Math.round(awayAttendance * scaleFactor);
        }
        return Map.of(
            "homeAttendance", homeAttendance,
            "awayAttendance", awayAttendance
        );
    }
}
