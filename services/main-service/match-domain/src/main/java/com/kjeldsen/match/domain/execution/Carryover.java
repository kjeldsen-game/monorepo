package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.match.domain.state.GameState;
import java.util.Map;

public class Carryover {

    /*
     * Determines the points to be carried over to the next duel
     */

    // Carryover from the previous duel is half of the difference of the total duel points of the
    // two players. The carryover value is given to the winner of the (previous) duel and
    // contributes to the (current) duel. This is capped at half of the maximum assistance.
    public static Map<DuelRole, Integer> getCarryover(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                int initiatorTotal = duel.getInitiatorStats() != null ? duel.getInitiatorStats().getTotal() : 0;
                int challengerTotal = duel.getChallengerStats() != null ? duel.getChallengerStats().getTotal() : 0;
                int diff = Math.abs(initiatorTotal - challengerTotal);
                int carryover = diff / 2;

                DuelRole winner =
                    initiatorTotal > challengerTotal
                        ? DuelRole.INITIATOR
                        : DuelRole.CHALLENGER;
                DuelRole loser =
                    (winner == DuelRole.INITIATOR)
                        ? DuelRole.CHALLENGER
                        : DuelRole.INITIATOR;

                // TODO limiting carryover
                return Map.of(
                    winner, carryover,
                    loser, 0
                );
            })
            .orElseGet(() -> Map.of(
                DuelRole.INITIATOR, 0,
                DuelRole.CHALLENGER, 0
            ));
    }
}
