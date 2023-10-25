package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.entities.Id;
import java.util.Map;
import java.util.stream.Collectors;

public class Probability {

    /*
     * Shared probability utilities for selection classes
     */

    // Calculates the actual probability that a player will be selected based on the values
    // assigned to their position
    public static Map<Id, Double> toProbabilityMap(Map<Id, Integer> values) {
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
    public static Id selectFromProbabilities(Map<Id, Double> probabilities) {
        double[] cumulativeProbabilities = new double[probabilities.size()];
        Id[] playerIds = new Id[probabilities.size()];
        int index = 0;
        double cumulativeProbability = 0.0;

        for (Map.Entry<Id, Double> entry : probabilities.entrySet()) {
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
