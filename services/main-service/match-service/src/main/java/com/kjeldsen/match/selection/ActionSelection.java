package com.kjeldsen.match.selection;

import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.GameStateException;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PitchArea.PitchRank;
import com.kjeldsen.player.domain.PlayerPosition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

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
        Action selectedAction = actions.get(new Random().nextInt(size));
        //log.info("Valid actions: {}, selected action:: {}", actions, selectedAction);
        return selectedAction;
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
            // Prevents forwards from passing the ball to area when in penalty box
            .filter(action ->
                !((action == Action.PASS )
                        && player.getPosition() == PlayerPosition.FORWARD
                        && pitchArea.rank() == PitchRank.FORWARD
                        && pitchArea.file() == PitchArea.PitchFile.CENTRE))
            // Prevents certain players from shooting
            .filter(action -> !(action == Action.SHOOT && shootingProhibited(player, pitchArea)))
            .toList();

        // If inside a chained action sequence, chose the only valid action.
        if (state.getChainActionSequence().isActive()) {
            Play lastPlay = state.lastPlay().get();
            switch (lastPlay.getDuel().getType()) {
                // If the last duel was positional, then the player must pass to the player that initiated the chained action sequence.
                case POSITIONAL ->  actions = actions.stream().filter(action -> action == Action.PASS).toList();
            }
        }

        if (actions.isEmpty()) {
            log.info("Current area: {}", pitchArea.toString());
            log.info("Legal actions:");
            legalActions.forEach(action -> log.info(action.toString()));
            throw new GameStateException(state,
                "No valid actions after action filters were applied");
        }
        return actions;
    }

    private static boolean dribblingProhibited(Player player, PitchArea pitchArea) {
        return pitchArea.rank() != PitchRank.MIDDLE;
    }

    private static boolean shootingProhibited(Player player, PitchArea pitchArea) {
        return !player.getPosition().isForward() || pitchArea.rank() != PitchRank.FORWARD;
    }
}
