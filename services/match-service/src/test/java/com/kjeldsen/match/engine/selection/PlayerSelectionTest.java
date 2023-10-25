package com.kjeldsen.match.engine.selection;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.player.Player;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerSelectionTest {

    @Test
    void noStrikerOnTeam() {
        List<Player> players = RandomHelper.genPlayers(Id.generate()).stream()
            .filter(p -> !p.getPosition().isForward())
            .toList();

        TeamState team = TeamState.builder()
            .players(players)
            .build();

        assertThrows(GameStateException.class,
            () -> KickOffSelection.selectPlayer(team));
    }
}
