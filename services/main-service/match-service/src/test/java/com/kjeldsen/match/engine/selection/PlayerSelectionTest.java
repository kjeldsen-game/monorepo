package com.kjeldsen.match.engine.selection;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.engine.entities.Player;
import com.kjeldsen.match.engine.entities.Team;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerSelectionTest {

    @Test
    void noStrikerOnTeam() {
        Team team = RandomHelper.genTeam();
        List<Player> players = RandomHelper.genPlayers(team).stream()
            .filter(p -> !p.getPosition().isForward())
            .toList();

        TeamState newTeam = TeamState.builder()
            .players(players)
            .build();

        GameState state = GameState.builder().build();
        assertThrows(GameStateException.class,
            () -> KickOffSelection.selectPlayer(state, newTeam));
    }
}
