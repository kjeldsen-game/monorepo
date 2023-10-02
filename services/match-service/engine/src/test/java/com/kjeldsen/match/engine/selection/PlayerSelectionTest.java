package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.utils.RandomHelper.genPlayers;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.PlayerPosition;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.TeamState;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerSelectionTest {

    @Test
    void noStrikerOnTeam() {
        List<Player> players = genPlayers(TeamId.generate()).stream()
            .filter(p -> !p.getPosition().equals(PlayerPosition.STRIKER)).toList();

        TeamState team = TeamState.builder()
            .players(players)
            .build();

        assertThrows(GameStateException.class,
            () -> PlayerSelection.selectKickOffPlayer(team));
    }
}
