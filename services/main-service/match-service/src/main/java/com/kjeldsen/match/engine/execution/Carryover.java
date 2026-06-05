package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Player;
import java.util.Map;

public class Carryover {

    /*
     * Determines the points to be carried over to the next duel
     */

    public static final int CARRYOVER_LIMIT = Assistance.MAX_ASSISTANCE / 2;

    // Carryover from the previous duel is half of the difference of the total duel points of the
    // two players, capped at CARRYOVER_LIMIT. The carryover value is keyed by the winning team's
    // ID so it is correctly attributed even when players change roles between duels (e.g. a
    // player who was CHALLENGER in the positional duel becomes INITIATOR in the tackle that
    // follows).
    public static Map<Long, Integer> getCarryover(GameState state) {
        return state.lastPlay()
            .map(Play::getDuel)
            .map(duel -> {
                int initiatorTotal = duel.getInitiatorStats().getTotal();
                int challengerTotal = duel.getChallengerStats().getTotal();
                int diff = Math.abs(initiatorTotal - challengerTotal);
                int carryover = Math.min(diff / 2, CARRYOVER_LIMIT);

                Player winner =
                    (duel.getChallenger() == null || initiatorTotal >= challengerTotal)
                        ? duel.getInitiator()
                        : duel.getChallenger();

                return Map.of(winner.getTeamId(), carryover);
            })
            .orElseGet(Map::of);
    }
}
