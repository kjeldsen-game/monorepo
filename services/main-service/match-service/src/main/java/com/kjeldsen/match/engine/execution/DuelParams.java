package com.kjeldsen.match.engine.execution;

import com.kjeldsen.match.engine.entities.duel.DuelOrigin;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Player;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DuelParams {

    GameState state;
    DuelType duelType;
    Player initiator;
    Player challenger;
    Player receiver;
    DuelOrigin origin;
}
