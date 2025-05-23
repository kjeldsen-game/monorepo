package com.kjeldsen.match.domain.selection;

import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelType;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.GameStateException;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.player.domain.PitchArea;
import com.kjeldsen.player.domain.PlayerPosition;
import java.util.List;

public class ChallengerSelection {

    /*
     * Selects challengers for duels.
     */

    // Returns a player from the opposing team to challenge the player in possession
    // of the ball
    // based on the duel type.
    public static Player selectChallenger(GameState state, DuelType duelType) {
        List<Player> players = state.defendingTeam().getPlayers();
        // For all challenger selection the pitch is flipped to match their perspective.
        // The
        // correctly oriented pitch area must be passed down through the selection
        // methods.

        // If there is free throw the Challenger is not present TODO review
        if (duelType.equals(DuelType.THROW_IN)) {
            return null;
        }

        PitchArea pitchArea = state.getBallState().getArea().flipPerspective();
        return switch (duelType) {
            case PASSING_LOW, PASSING_HIGH -> passingDuelChallenger(state, pitchArea);
            case DRIBBLE -> dribbleDuelChallenger(state, pitchArea);
            case BALL_CONTROL -> ballControlDuelChallenger(state);
            case POSITIONAL -> positionalDuelChallenger(state, pitchArea);
            case LOW_SHOT, ONE_TO_ONE_SHOT, HEADER_SHOT, LONG_SHOT -> players.stream()
                    .filter(p -> p.getPosition() == PlayerPosition.GOALKEEPER)
                    .findAny()
                    .orElseThrow(() -> new GameStateException(state, "No goalkeeper found"));
            case THROW_IN -> null;
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

    // Returns a player from the defending team to intercept the plauer.
    public static Player dribbleDuelChallenger(GameState state, PitchArea pitchArea) {
        // Passing duels always succeed for now so select any nearby player
        return state.defendingTeam().getPlayers().stream()
                .filter(challenger -> challenger.getPosition() != PlayerPosition.GOALKEEPER)
                .filter(challenger -> challenger.getPosition().coverage().contains(pitchArea))
                .findAny()
                .orElseThrow(
                        () -> new GameStateException(state, "No players found to intercept the player"));
    }

    // Returns a player from the defending team to challenge the player in a ball
    // control duel.
    public static Player ballControlDuelChallenger(GameState state) {
        // The ball control duel happens directly after a positional duel is lost, so
        // the challenger
        // here is the player that started and lost the positional duel
        Player player = state.lastPlay()
            // If the last play resulted in a win, then return null (no ball control duel is
            // needed)
            .filter(play -> !play.getDuel().getResult().equals(DuelResult.WIN))
            .map(play -> play.getDuel().getChallenger())
            .orElse(null);
        return player != null ? state.defendingTeam().getPlayerById(player.getId()) : null;
    }

    // Returns a defender to counter the challenger in a positional duel.
    public static Player positionalDuelChallenger(GameState state, PitchArea pitchArea) {
        return switch (pitchArea.rank()) {
            // If the ball is in the forward area, positional duels are played but there is
            // no challenger
            case FORWARD -> null;
            case MIDDLE -> DefenderSelection.selectFromMidfield(state, pitchArea);
            case BACK -> DefenderSelection.selectFromBack(state, pitchArea);
        };
    }
}
