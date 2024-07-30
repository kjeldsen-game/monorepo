package com.kjeldsen.match.execution;

import com.kjeldsen.match.entities.duel.DuelOrigin;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PlayerOrder;
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
    PlayerOrder appliedPlayerOrder;
}
