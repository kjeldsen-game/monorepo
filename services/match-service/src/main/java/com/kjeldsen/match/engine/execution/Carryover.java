package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.engine.entities.duel.DuelType;

public class Carryover {

    /*
     * Determines the points to be carried over to the next duel
     */

    public static final int CARRYOVER_LIMIT = 25;

    // For ball control duels, the carryover from the previous (positional) duel is the difference
    // in assistance between the two players in that duel, capped at the carryover limit. This value
    // is give to the winner of that duel for the ball control duel.
    public static int fromPositionalDuel(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                if (duel.getType() != DuelType.POSITIONAL) {
                    throw new GameStateException(state, "The last duel was not a positional duel");
                } else {
                    int diff =
                        duel.getInitiatorStats().getAssistance()
                            - duel.getChallengerStats().getAssistance();
                    return Math.max(Math.min(diff, CARRYOVER_LIMIT), -CARRYOVER_LIMIT);
                }
            })
            .orElseThrow(() -> new GameStateException(state, "No plays have been made yet"));
    }

    // For non-positional duel, the carryover is calculated not from the difference in assistance
    // (which is carried over only to ball control duels) but from the total difference. This value
    // is given to the winner of that duel over for the next duel.
    public static int fromPreviousDuel(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                if (duel.getType() == DuelType.POSITIONAL && duel.getResult() == DuelResult.LOSE) {
                    throw new GameStateException(
                        state,
                        "Carryover to ball control duels should come from the positional duel");
                } else {
                    int diff =
                        duel.getInitiatorStats().getTotal() - duel.getChallengerStats().getTotal();
                    return Math.max(Math.min(diff, CARRYOVER_LIMIT), -CARRYOVER_LIMIT);
                }
            })
            .orElseThrow(() -> new GameStateException(state, "No plays have been made yet"));
    }
}
