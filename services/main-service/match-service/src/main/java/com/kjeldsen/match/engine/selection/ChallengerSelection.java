package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.models.Player;
import java.util.List;
import java.util.Optional;

public class ChallengerSelection {

    /*
     * Selects challengers for duels.
     */

    // Returns a player from the opposing team to challenge the player in possession of the ball
    // based on the duel type.
    public static Player selectChallenger(GameState state, DuelType duelType) {
        List<Player> players = state.defendingTeam().getPlayers();
        // For all challenger selection the pitch is flipped to match their perspective. The
        // correctly oriented pitch area must be passed down through the selection methods.
        PitchArea pitchArea = state.getBallState().getArea().flipPerspective();
        return switch (duelType) {
            case PASSING -> passingDuelChallenger(state, pitchArea);
            case BALL_CONTROL -> ballControlDuelChallenger(state);
            case POSITIONAL -> positionalDuelChallenger(state, pitchArea);
            case SHOT -> players.stream()
                .filter(p -> p.getPosition() == PlayerPosition.GOALKEEPER)
                .findAny()
                .orElseThrow(() -> new GameStateException(state, "No goalkeeper found"));
        };
    }

    // Returns a player from the defending team to intercept the ball.
    public static Player passingDuelChallenger(GameState state, PitchArea pitchArea) {
        // Passing duels always succeed for now so select any nearby player
        return state.defendingTeam().getPlayers().stream()
            .filter(challenger -> challenger.getPosition() != PlayerPosition.GOALKEEPER)
            .filter(challenger -> challenger.getPosition().coverage().contains(pitchArea))
            .findAny()
            .orElseThrow(
                () -> new GameStateException(state, "No players found to intercept the ball"));
    }

    // Returns a player from the defending team to challenge the player in a ball control duel.
    public static Player ballControlDuelChallenger(GameState state) {
        // The ball control duel happens directly after a positional duel is lost, so the challenger
        // here is the player that started and lost the positional duel
        return state.lastPlay()
            .map(play -> play.getDuel().getInitiator())
            .orElseThrow(
                () -> new GameStateException(state, "A positional duel must be played first"));
    }

    // Returns a defender to counter the challenger in a positional duel.
    public static Player positionalDuelChallenger(GameState state, PitchArea pitchArea) {
        return switch (pitchArea.rank()) {
            // If the ball is in the forward area, positional duels should not be played. Defenders
            // in control of the ball should pass it forward.
            case FORWARD -> throw new GameStateException(state, "No defenders in forward area");
            case MIDDLE -> DefenderSelection.selectFromMidfield(state, pitchArea);
            case BACK -> DefenderSelection.selectFromBack(state, pitchArea);
        };
    }
}
