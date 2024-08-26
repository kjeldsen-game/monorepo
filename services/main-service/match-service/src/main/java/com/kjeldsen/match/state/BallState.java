package com.kjeldsen.match.state;

import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import lombok.Value;

@Value
public class BallState {

    /*
     * Represents the position of the ball and which player is in control of it.
     */
    Player player;

    PitchArea area;

    BallHeight height;

    public static BallState init() {
        return new BallState(null, null, null);
    }
}
