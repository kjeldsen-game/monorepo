package com.kjeldsen.match.selection;

import com.kjeldsen.match.state.GameStateException;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.entities.Player;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Probability {

    /*
     * Shared probability utilities for selection classes
     */

    // Given a list of candidate players, returns one based on a map of their relative value, which
    // represents how likely they are to be selected
    public static Player drawPlayer(
        GameState state, List<Player> candidates, Map<String, Integer> values) {

        Map<String, Double> probabilities = toProbabilityMap(values);
        String selectedPlayerId = selectFromProbabilities(state, probabilities);
        return candidates.stream()
            .filter(p -> Objects.equals(p.getId(), selectedPlayerId))
            .findAny()
            .orElseThrow(() ->
                // This error indicates that the values map does not match the candidates list
                new GameStateException(state, "Player could not be drawn from candidates list"));
    }

    // Calculates the actual probability that a player will be selected based on the values
    // assigned to their position
    private static Map<String, Double> toProbabilityMap(Map<String, Integer> values) {
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
    private static String selectFromProbabilities(GameState state, Map<String, Double> probabilities) {
        double[] cumulativeProbabilities = new double[probabilities.size()];
        String[] playerIds = new String[probabilities.size()];
        int index = 0;
        double cumulativeProbability = 0.0;

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
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
        throw new GameStateException(state, "Could not draw a player from probabilities");
    }
}
