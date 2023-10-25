package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.duel.DuelRole;
import com.kjeldsen.match.entities.player.Player;
import java.util.List;
import java.util.Random;

public class PitchAreaSelection {

    /*
     * Players can cover more than one pitch area based on their position, but a definite area must
     * be assumed by each player when they are involved in a duel. From a list of possible pitch
     * area they are allowed to play in, this class selects a single pitch area for each player.
     */

    // For now returns a random area from the player's pitch area coverage that is near the ball.
    // The `role` parameter is the role played in the last duel.
    public static PitchArea select(GameState state, Player player, DuelRole role) {
        PitchArea ballArea = state.getBallState().getArea();
        List<PitchArea> areas = player.getPosition().coverage().stream()
            .filter(area ->
                switch (role) {
                    case INITIATOR -> ballArea.teammateIsNearby(area);
                    case CHALLENGER -> ballArea.opponentIsNearby(area);
                })
            .toList();
        return areas.get(new Random().nextInt(areas.size()));
    }
}
