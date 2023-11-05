package com.kjeldsen.match.engine.processing;

import com.kjeldsen.match.engine.entities.MatchResult;

public final class Ratings {

    /*
     * Calculates rating and skill level changes of the teams and players after a match.
     */

    // Returns the new Elo ratings of the two teams given a result
    public static Pair<Integer, Integer> eloRatings(
        int rating1, int rating2, MatchResult matchResult) {

        final double K = 20.0;

        double expectedEloScore = expectedEloScore(rating1, rating2);
        double observedEloScore = matchResult.score();

        int scoreA = (int) Math.round(rating1 + K * (observedEloScore - expectedEloScore));
        int scoreB = (int) Math.round(rating2 + K * (expectedEloScore - observedEloScore));

        return new Pair<>(scoreA, scoreB);
    }

    private static double expectedEloScore(double rating1, double rating2) {
        double diff = (rating2 - rating1) / 400.0;
        return 1.0 / (1.0 + Math.pow(10.0, diff));
    }
}
