package com.kjeldsen.match.engine.state;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.Player;
import lombok.Value;

@Value
public class BallState {

    /*
     * Represents the position of the ball and which player is in control of it.
     */

    Player player;

    PitchArea area;

    public static BallState init() {
        return new BallState(null, null);
    }
}
