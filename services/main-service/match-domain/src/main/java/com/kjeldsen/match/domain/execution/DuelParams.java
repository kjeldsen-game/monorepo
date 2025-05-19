package com.kjeldsen.match.domain.execution;

import com.kjeldsen.match.domain.entities.duel.DuelDisruptor;
import com.kjeldsen.match.domain.entities.duel.DuelOrigin;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerOrder;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class DuelParams {

    GameState state;
    DuelType duelType;
    Player initiator;
    Player challenger;
    Player receiver;
    DuelOrigin origin;
    DuelDisruptor disruptor;
    PlayerOrder appliedPlayerOrder;
    PitchArea destinationPitchArea;

    // Candidates for new values in duel parameters that could happened during duel execution
    PitchArea candidatePitchArea;
    Player candidateChallenger;
    Player candidateReceiver;
}
