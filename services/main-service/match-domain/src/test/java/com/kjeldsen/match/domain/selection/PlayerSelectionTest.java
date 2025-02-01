package com.kjeldsen.match.domain.selection;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerSelectionTest {

    @Test
    void noStrikerOnTeam() {
        Team team = RandomHelper.genTeam(TeamRole.HOME);
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
