package com.kjeldsen.player.domain.generator;

public class OvertrainingGenerator {
    private static final Float slowPercentage = 0.3f;
    //! Overtraining possible way to administrate the points of the phase.
    public static Integer generateOvertrainingRaise(Integer currentDay) {

        double probability = probabilityOvertrainingRiseBasedOnCurrentDay(currentDay, slowPercentage);
        double randomProbabilityRise = RandomGenerator.random() * 100;

        if (randomProbabilityRise <= probability) {
            return generateOvertrainingPoints();
        } else {
            return 0;
        }
    }
    public static double probabilityOvertrainingRiseBasedOnCurrentDay(Integer currentDay, Float slowPercentage) {
        return switch (currentDay) {
            case 1 -> 2.03 * slowPercentage;
            case 2 -> 2.73 * slowPercentage;
            case 3 -> 3.69 * slowPercentage;
            case 4 -> 4.98 * slowPercentage;
            case 5 -> 6.73 * slowPercentage;
            case 6 -> 9.08 * slowPercentage;
            case 7 -> 12.26 * slowPercentage;
            case 8 -> 16.55 * slowPercentage;
            case 9 -> 22.34 * slowPercentage;
            case 10 -> 30.16 * slowPercentage;
            case 11 -> 40.72 * slowPercentage;
            case 12 -> 54.97 * slowPercentage;
            case 13 -> 74.20 * slowPercentage;
            case 14 -> 100 * slowPercentage;
            default -> throw new IllegalStateException("Unexpected value (from 1 - 14 days): " + currentDay);
        };
    }

    public static int generateOvertrainingPoints() {

        double randomProbabilityPoints = RandomGenerator.random() * 100;

        if (randomProbabilityPoints <= 35) {
            return 1;
        } else if (randomProbabilityPoints > 35 && randomProbabilityPoints <= 70) {
            return 2;
        } else if (randomProbabilityPoints > 70 && randomProbabilityPoints <= 85) {
            return 3;
        } else if (randomProbabilityPoints > 85 && randomProbabilityPoints <= 95) {
            return 4;
        } else {
            return 5;
        }
    }
}
