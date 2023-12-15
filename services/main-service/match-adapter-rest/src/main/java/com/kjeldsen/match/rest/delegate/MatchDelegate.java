package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.CreateMatchRequest;
import com.kjeldsen.match.rest.model.Modifiers;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerOrder;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MatchDelegate implements MatchApiDelegate {

    private final TeamReadRepository teamRepo;
    private final PlayerReadRepository playerRepo;

    /*
     * The match service uses a different internal representation of teams and players so here
     * these entities are mapped from the database version to the engine version, which also
     * includes modifiers that differ for each match (these are passed as match creation params).
     */

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest request) {
        TeamId homeId = TeamId.of(request.getHome().getId());
        com.kjeldsen.player.domain.Team home = teamRepo.findById(homeId)
            .orElseThrow(() -> new RuntimeException("Home team not found"));

        TeamId awayId = TeamId.of(request.getAway().getId());
        com.kjeldsen.player.domain.Team away = teamRepo.findById(awayId)
            .orElseThrow(() -> new RuntimeException("Away team not found"));

        Team engineHome = buildTeam(home, request.getHome().getModifiers());
        Team engineAway = buildTeam(away, request.getAway().getModifiers());

        Match match = Match.builder()
            .home(engineHome)
            .away(engineAway)
            .build();

        GameState state = Game.play(match);
        MatchReport report = new MatchReport(state, state.getPlays(), engineHome, engineAway);
        match.setMatchReport(report);

        String json = JsonUtils.exclude(
            report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Team buildTeam(com.kjeldsen.player.domain.Team home, Modifiers modifiers) {
        List<Player> enginePlayers = playerRepo.findByTeamId(home.getId()).stream()
            .map(this::buildPlayer)
            .toList();

        return Team.builder()
            .id(home.getId().value())
            .players(enginePlayers)
            .tactic(Tactic.valueOf(modifiers.getTactic().getValue()))
            .verticalPressure(VerticalPressure.valueOf(modifiers.getVerticalPressure().getValue()))
            .horizontalPressure(
                HorizontalPressure.valueOf(modifiers.getHorizontalPressure().getValue()))
            .build();
    }

    private Player buildPlayer(com.kjeldsen.player.domain.Player player) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return Player.builder()
            .id(player.getId().value())
            .name(player.getName())
            .teamId(player.getTeamId().value())
            .position(player.getPosition())
            .skills(skills)
            .playerOrder(player.getPlayerOrder())
            .build();
    }
}
