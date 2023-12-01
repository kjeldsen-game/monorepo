package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PitchArea.PitchRank;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.models.Player;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionSelection {

    /*
     * Selects actions to be performed by players. Currently only for the active player, but actions
     * for responding to the active player's action would be here.
     */

    // Starts with a list of legal actions and filters them into a list of more sensible valid
    // actions, based on pitch area and player position, then chooses one randomly.
    public static Action selectAction(GameState state, Player player) {
        List<Action> legalActions = state.legalActions();
        List<Action> actions = filterActions(legalActions, state, player);
        int size = actions.size();
        return actions.get(new Random().nextInt(size));
    }

    static List<Action> filterActions(List<Action> legalActions, GameState state, Player player) {
        // After a goal or at kick off the only valid action is a pass. This needs to be here since
        // the filters below prohibit forwards from passing.
        if (legalActions.size() == 1 && legalActions.get(0) == Action.PASS) {
            return List.of(Action.PASS);
        }
        PitchArea pitchArea = state.getBallState().getArea();

        List<Action> actions = legalActions.stream()
            // Prevents players in back area from positioning - this is required to force defensive
            // midfielders (who have receded into the back area) to pass the ball forward.
            .filter(action -> !(action == Action.POSITION && pitchArea.rank() == PitchRank.BACK))
            // Prevents forwards from passing the ball
            .filter(action ->
                !(action == Action.PASS && player.getPosition() == PlayerPosition.FORWARD))
            // Prevents certain players from shooting
            .filter(action -> !(action == Action.SHOOT && shootingProhibited(player, pitchArea)))
            .toList();

        if (actions.isEmpty()) {
            log.info("Current area: {}", pitchArea.toString());
            log.info("Legal actions:");
            legalActions.forEach(action -> log.info(action.toString()));
            throw new GameStateException(state,
                "No valid actions after action filters were applied");
        }
        return actions;
    }

    private static boolean shootingProhibited(Player player, PitchArea pitchArea) {
        return !player.getPosition().isForward() || pitchArea.rank() != PitchRank.FORWARD;
    }
}
