package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.player.Player;
import com.kjeldsen.match.entities.player.PlayerPosition;
import java.util.List;

public class ChallengerSelection {

    /*
     * Selects challengers for duels.
     */

    // Returns a player from the opposing team to challenge the player in possession of the ball
    // based on the duel type.
    public static Player selectChallenger(GameState state, DuelType duelType) {
        List<Player> players = state.defendingTeam().getPlayers();
        return switch (duelType) {
            case PASSING -> selectPassingDuelChallenger(state);
            case BALL_CONTROL -> selectBallControlDuelChallenger(state);
            case POSITIONAL -> selectPositionalDuelChallenger(state);
            case SHOT -> players.stream()
                .filter(p -> p.getPosition() == PlayerPosition.GOALKEEPER)
                .findAny()
                .orElseThrow(() -> new GameStateException("No goalkeeper found"));
        };
    }

    // Returns a player from the defending team to intercept the ball.
    public static Player selectPassingDuelChallenger(GameState state) {
        // Passing duels always succeed for now so select any nearby player
        PitchArea ballArea = state.getBallState().getArea();
        return state.defendingTeam().getPlayers().stream()
            .filter(challenger ->
                challenger.getPosition().coverage().stream().anyMatch(ballArea::opponentIsNearby)
            ).findAny()
            .orElseThrow(() -> new GameStateException("No players found to intercept the ball"));
    }

    // Returns a player from the defending team to challenge the player in a ball control duel.
    public static Player selectBallControlDuelChallenger(GameState state) {
        // The ball control duel happens directly after a positional duel is lost, so the challenger
        // here is the player that started and lost the positional duel
        return state.lastPlay()
            .map(play -> play.getDuel().getInitiator())
            .orElseThrow(() -> new GameStateException("A positional duel must be played first"));
    }

    // Returns a defender to counter the challenger in a positional duel.
    public static Player selectPositionalDuelChallenger(GameState state) {
        PitchArea ballArea = state.getBallState().getArea();
        return switch (ballArea.rank()) {
            // A defender should never be required if the ball is in the back area
            case BACK -> throw new GameStateException("No defenders in the back area");
            case MIDDLE -> DefenderSelection.selectDefenderForMidfield(state);
            case FORWARD -> DefenderSelection.selectDefenderForForward(state);
        };
    }
}
