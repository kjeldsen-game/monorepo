package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.PitchArea.PitchRank;
import com.kjeldsen.match.entities.player.Player;
import java.util.List;
import java.util.Random;

public class ActionSelection {

    /*
     * Selects actions to be performed by players. Currently only for the active player, but actions
     * for responding to the active player's action would be here.
     */

    // Starts with a list of legal actions and filters them into a list of more sensible valid
    // actions, based on pitch area and player position, then chooses one randomly.
    public static Action selectAction(GameState state, Player player) {
        List<Action> legalActions = state.legalActions();

        // After a goal or at kick off the only valid action is a pass. This needs to be here since
        // the filters below prohibit forwards from passing.
        if (legalActions.size() == 1 && legalActions.get(0) == Action.PASS) {
            return Action.PASS;
        }

        PitchArea pitchArea = state.getBallState().getArea();
        List<Action> actions = legalActions.stream()
            .filter(action -> !(player.getPosition().isForward() && action == Action.PASS))
            .filter(action -> !(action == Action.SHOOT && !shootingAllowed(player, pitchArea)))
            .toList();

        if (actions.isEmpty()) {
            throw new GameStateException("No valid actions after action filters were applied");
        }
        int size = actions.size();
        return actions.get(new Random().nextInt(size));
    }

    private static boolean shootingAllowed(Player player, PitchArea pitchArea) {
        return player.getPosition().isForward() && pitchArea.rank() == PitchRank.FORWARD;
    }
}
