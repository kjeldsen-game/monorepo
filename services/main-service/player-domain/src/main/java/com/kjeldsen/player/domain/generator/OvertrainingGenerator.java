package com.kjeldsen.player.domain.generator;

public class OvertrainingGenerator {

    //! Overtraining posible way to administrate the points of the overtraining phase.
    public static Integer generateOvertrainingRaise() {

        double randomProbabilityPercentage = RandomGenerator.random() * 100;

        if (randomProbabilityPercentage < 8) {
            return 1;
        } else if (randomProbabilityPercentage >= 8 && randomProbabilityPercentage < 14) {
            return 2;
        } else if (randomProbabilityPercentage > 14 && randomProbabilityPercentage <= 18) {
            return 3;
        } else if (randomProbabilityPercentage > 18 && randomProbabilityPercentage <= 20) {
            return 4;
        } else {
            return 0;
        }
    }
}
