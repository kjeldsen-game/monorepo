package com.kjeldsen.match.entities.duel;

import com.kjeldsen.match.entities.DuelStats;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Duel {

    DuelType type;
    // The location of the duel on the pitch is determined by the location of the ball when the
    // duel was initiated.
    PitchArea pitchArea;
    // The player who started the duel (the attacking player) is called the 'initiator' to avoid
    // confusion with the player position 'attacker'.
    Player initiator;
    // The player who defends the duel is similarly called the 'challenger' to avoid confusion with
    // the 'defender' position.
    Player challenger;
    // The player who receives the ball (in the case of a successful duel) is the 'receiver'. This
    // applies only to actions such as passes, not shots.
    Player receiver;
    DuelResult result;
    // Numerical details about the duel
    DuelStats initiatorStats;
    DuelStats challengerStats;
    // Whether the duel originates from anything other than default play
    DuelOrigin origin;
}
