package com.kjeldsen.match.domain.selection;

import com.kjeldsen.match.domain.entities.Action;
import com.kjeldsen.match.domain.entities.Play;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.BallHeight;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.player.domain.PlayerReceptionPreference;

import java.util.Random;

public class DuelTypeSelection {

    /*
     * Selects duel type for and action.
     */
    public static DuelType select(GameState state, Action action, Player receiver) {

        return switch (action) {
            case PASS -> {
                if (PlayerReceptionPreference.DEMAND_LOW.equals(receiver.getReceptionPreference())) {
                    yield DuelType.PASSING_LOW;
                }
                if (PlayerReceptionPreference.DEMAND_HIGH.equals(receiver.getReceptionPreference())) {
                    yield DuelType.PASSING_HIGH;
                }
                // PlayerReceptionPreference.MIXED
                // If there is no player preference, default to MIXED.
                int lowChance = new Random().nextInt(0, 100);
                if (lowChance < 50) {
                    yield DuelType.PASSING_LOW;
                } else {
                    yield DuelType.PASSING_HIGH;
                }
            }
            case DRIBBLE -> DuelType.DRIBBLE;
            case POSITION -> DuelType.POSITIONAL;
            // TACKLE is the ball control action: if the challenger of the preceding positional duel
            // won, then they initiate the following ball control duel, so the action is a tackle
            // and not a dribble since actions are framed from the perspective of the initiator.
            case TACKLE -> DuelType.BALL_CONTROL;
            case SHOOT -> {
                DuelType result = DuelType.LOW_SHOT;

                Play lastPlay = null;
                if (state.lastPlay().isPresent()) {
                    lastPlay = state.lastPlay().get();
                    // does not work with last play, as player order y being set at this moment.
//                    if (DuelOrigin.PLAYER_ORDER.equals(lastPlay.getDuel().getOrigin())) {
//                        yield DuelType.LONG_SHOT;
//                    }
                    if (BallHeight.HIGH.equals(lastPlay.getBallState().getHeight())) {
                        yield DuelType.HEADER_SHOT;
                    }
                    if (DuelType.POSITIONAL.equals(lastPlay.getDuel().getType())) {
                        yield DuelType.ONE_TO_ONE_SHOT;
                    }
                    if (DuelType.BALL_CONTROL.equals(lastPlay.getDuel().getType())) {
                        yield DuelType.LOW_SHOT;
                    }
                }

                yield result;
            }
        };

    }
}
