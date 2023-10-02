package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.type.DuelType;
import com.kjeldsen.match.domain.type.PitchArea;
import com.kjeldsen.match.domain.type.PlayerPosition;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.TeamState;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSelection {

    /*
     * A miscellaneous class responsible for selecting players for various actions and duels.
     * For specific/complex cases (e.g. receiver selection) use one of the other selection classes.
     * These methods may also call such classes if here we are handling broad use cases.
     */

    // Returns the player to start the game.
    public static Player selectKickOffPlayer(TeamState team) {
        // For not return any striker
        return team.getPlayers().stream()
            .filter(p -> p.getPosition().equals(PlayerPosition.STRIKER))
            .findAny()
            .orElseThrow(() -> new GameStateException("No striker found to initiate kick off"));
    }

    // Returns a player from the opposing team to challenge the player in possession of the ball
    // based on the duel type.
    public static Player selectChallenger(GameState state, DuelType duelType) {
        List<Player> players = state.getDefendingTeam().getPlayers();
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

    // Returns a player from the attacking team to intercept the ball.
    public static Player selectPassingDuelChallenger(GameState state) {
        // Passing duels always succeed for now so select any player, but in the future a player
        // is selected here to try to intercept the ball
        return state.getDefendingTeam().getPlayers().stream()
            .findAny()
            .orElseThrow(
                () -> new GameStateException("No players found to challenge passing duel"));
    }

    // Returns a player from the defending team to challenge the player in a ball control duel.
    public static Player selectBallControlDuelChallenger(GameState state) {
        // The ball control duel happens directly after a positional duel is lost, so the challenger
        // here is the player that started and lost the positional duel
        return state.getPlays()
            .get(state.getPlays().size() - 1)
            .getDuel()
            .getInitiator();
    }

    // Returns a defender to counter the challenger in a positional duel.
    public static Player selectPositionalDuelChallenger(GameState state) {
        PitchArea ballArea = state.getBallState().getArea();

        // A defender should never be required if the ball is in the back area
        return switch (ballArea) {
            case BACK -> throw new GameStateException("No defenders in the back area");
            case MIDFIELD ->
                DefenderSelection.selectDefenderForMidfield(state.getDefendingTeam().getPlayers());
            case FORWARD ->
                DefenderSelection.selectDefenderForForward(state.getDefendingTeam().getPlayers());
        };
    }

    /*
     * Below are some probability utilities used for player selection
     */

    // Calculates the actual probability that a player will be selected based on the values
    // assigned to their position
    public static Map<PlayerId, Double> toProbabilityMap(Map<PlayerId, Integer> values) {
        int sum = values.values().stream().reduce(0, Integer::sum);
        return values.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (double) entry.getValue() / sum));
    }

    // Returns a player based on the probability that they will be selected.
    // The probabilities param is a map of player ids and their probability of being selected.
    // The technique used here is based on calculating cumulative probabilities and then
    // generating a random number between 0 and 1. This number will fall within one of the
    // cumulative probability ranges and the player associated with that range will be selected.
    public static PlayerId selectFromProbabilities(Map<PlayerId, Double> probabilities) {
        double[] cumulativeProbabilities = new double[probabilities.size()];
        PlayerId[] playerIds = new PlayerId[probabilities.size()];
        int index = 0;
        double cumulativeProbability = 0.0;

        for (Map.Entry<PlayerId, Double> entry : probabilities.entrySet()) {
            playerIds[index] = entry.getKey();
            cumulativeProbability += entry.getValue();
            cumulativeProbabilities[index] = cumulativeProbability;
            index++;
        }

        double random = Math.random();
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (cumulativeProbabilities[i] > random) {
                return playerIds[i];
            }
        }

        // Should never be thrown if the probabilities are calculated correctly
        throw new GameStateException("Could not draw a player from probabilities");
    }
}
