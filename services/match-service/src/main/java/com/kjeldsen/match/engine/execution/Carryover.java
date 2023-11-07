package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import org.apache.commons.lang3.tuple.Pair;

public class Carryover {

    /*
     * Determines the points to be carried over to the next duel
     */

    public static final int CARRYOVER_LIMIT = 25;

    // For ball control duels, the carryover from the previous (positional) duel is calculated from
    // the difference in assistance between the two players in that duel, capped at the carryover
    // limit. This value is given to the winner of the (previous) duel and contributes to the
    // (current) ball control duel.
    public static Pair<DuelRole, Integer> fromPositionalDuel(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                if (duel.getType() != DuelType.POSITIONAL) {
                    throw new GameStateException(state, "The last duel was not a positional duel");
                } else {
                    int initiatorAssistance = duel.getInitiatorStats().getAssistance();
                    int challengerAssistance = duel.getChallengerStats().getAssistance();
                    int diff = Math.abs(initiatorAssistance - challengerAssistance);
                    DuelRole winner =
                        initiatorAssistance > challengerAssistance
                            ? DuelRole.INITIATOR
                            : DuelRole.CHALLENGER;
                    return Pair.of(winner, diff / 2);
                }
            })
            .orElseThrow(() -> new GameStateException(state, "No plays have been made yet"));
    }

    // For non-positional duel, the carryover is calculated not from the difference in assistance
    // (which is carried over only to ball control duels) but from the total difference. This value
    // is given to the winner of that duel over for the next duel.
    public static Pair<DuelRole, Integer> fromPreviousDuel(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                if (duel.getType() == DuelType.POSITIONAL && duel.getResult() == DuelResult.LOSE) {
                    throw new GameStateException(
                        state,
                        "Carryover to ball control duels should come from the positional duel");
                } else {

                    int initiatorTotal = duel.getInitiatorStats().getTotal();
                    int challengerTotal = duel.getChallengerStats().getTotal();
                    int diff = Math.abs(initiatorTotal - challengerTotal);
                    DuelRole winner =
                        initiatorTotal > challengerTotal
                            ? DuelRole.INITIATOR
                            : DuelRole.CHALLENGER;
                    return Pair.of(winner, Math.min(diff, CARRYOVER_LIMIT));
                }
            })
            .orElseThrow(() -> new GameStateException(state, "No plays have been made yet"));
    }
}
