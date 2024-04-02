package com.kjeldsen.player.domain.generator;

public class OvertrainingGenerator {
    private static final Float slowPercentage = 0.3f;
    public static Integer generateOvertrainingRaise(Integer currentDay) {

        double probability = probabilityOvertrainingRiseBasedOnCurrentDay(currentDay, slowPercentage);
        double randomProbabilityRise = RandomGenerator.random() * 100;

        if (randomProbabilityRise <= probability) {
            return 1;
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
}
