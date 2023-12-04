package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.controllers.ValidationException;
import com.kjeldsen.match.engine.Game;
import com.kjeldsen.match.engine.entities.Match;
import com.kjeldsen.match.engine.entities.MatchReport;
import com.kjeldsen.match.engine.entities.Player;
import com.kjeldsen.match.engine.entities.Team;
import com.kjeldsen.match.engine.modifers.PlayerOrder;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.CreateMatchRequest;
import com.kjeldsen.match.utils.JsonUtils;
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
    public ResponseEntity<Void> createMatch(CreateMatchRequest createMatchRequest) {
        TeamId homeId = TeamId.of(createMatchRequest.getHome().getId());
        com.kjeldsen.player.domain.Team home = teamRepo.findById(homeId)
            .orElseThrow(() -> new ValidationException("Home team not found"));

        TeamId awayId = TeamId.of(createMatchRequest.getAway().getId());
        com.kjeldsen.player.domain.Team away = teamRepo.findById(awayId)
            .orElseThrow(() -> new ValidationException("Away team not found"));

        Team engineHome = buildTeam(home);
        Team engineAway = buildTeam(away);

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

    private Team buildTeam(com.kjeldsen.player.domain.Team home) {
        List<Player> enginePlayers = playerRepo.findByTeamId(home.getId()).stream()
            .map(this::buildPlayer)
            .toList();

        return Team.builder()
            .id(home.getId().value())
            .players(enginePlayers)
            .build();
    }

    private Player buildPlayer(com.kjeldsen.player.domain.Player player) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));

        return Player.builder()
            .id(player.getId().value())
            .teamId(player.getTeamId().value())
            .position(player.getPosition())
            .skills(skills)
            .playerOrder(PlayerOrder.NONE)
            .build();
    }
}
