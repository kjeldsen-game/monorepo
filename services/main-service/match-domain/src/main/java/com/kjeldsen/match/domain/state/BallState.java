package com.kjeldsen.match.domain.state;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
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
