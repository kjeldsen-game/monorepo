package com.kjeldsen.match.selection;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.state.GameStateException;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.TeamState;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerSelectionTest {

    @Test
    void noStrikerOnTeam() {
        Team team = RandomHelper.genTeam();
        List<Player> players = RandomHelper.genActivePlayers(team).stream()
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
