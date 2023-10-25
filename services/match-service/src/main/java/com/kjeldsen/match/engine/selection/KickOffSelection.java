package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.TeamState;
import com.kjeldsen.match.entities.player.Player;

public class KickOffSelection {

    /*
     * Selects player to start the game or restart play after a goal
     */

    public static Player selectPlayer(TeamState team) {
        return team.getPlayers().stream()
            .filter(p -> p.getPosition().isForward())
            .findAny()
            .orElseThrow(
                () -> new GameStateException("No forward players found for kick off"));
    }
}
