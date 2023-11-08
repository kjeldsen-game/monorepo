package com.kjeldsen.match.engine.entities.duel;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.models.Player;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Duel {

    // TODO Refactor - don't store entire player object here

    DuelType type;
    // The player who started the duel (the attacking player) is called the 'initiator' to avoid
    // confusion with the player position 'attacker'.
    Player initiator;
    // The player who defends the duel is similarly called the 'challenger' to avoid confusion with
    // the 'defender' position.
    Player challenger;
    // The player who receives the ball (in the case of a successful duel) is the 'receiver'. This
    // applies only to actions such as passes, not shots.
    Player receiver;
    // The location of the duel on the pitch is determined by the location of the ball when the
    // duel was initiated.
    PitchArea pitchArea;
    DuelResult result;
    // Numerical details about the duel
    DuelStats initiatorStats;
    DuelStats challengerStats;
}
