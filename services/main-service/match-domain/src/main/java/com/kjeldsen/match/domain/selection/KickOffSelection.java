package com.kjeldsen.match.domain.selection;

import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.match.domain.entities.Player;

public class KickOffSelection {

    /*
     * Selects player to start the game or restart play after a goal
     */

    public static Player selectPlayer(GameState state, TeamState team) {
        return team.getPlayers().stream()
            .filter(p -> p.getPosition().isForward())
            .findAny()
            .orElseThrow(
                () -> new GameStateException(state, "No forward players found for kick off"));
    }
}
