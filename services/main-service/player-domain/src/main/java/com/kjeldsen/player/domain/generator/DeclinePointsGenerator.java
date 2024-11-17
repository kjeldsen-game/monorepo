package com.kjeldsen.player.domain.generator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeclinePointsGenerator {

    private static final Double BASE_VALUE = 7.14;

    public static int generateDeclinePoints(int currentDay, int year) {
        double variance = varianceIncreaseBasedOnYear(year);
        double probability = BASE_VALUE + variance * ( currentDay - 1);

        double randomProbability = RandomGenerator.random() * 100;
        if (randomProbability <= probability) {
            return 1;
        } else {
            return 0;
        }
    }


    public static double varianceIncreaseBasedOnYear(int year) {
        return switch (year) {
            case 1 -> 0.97;
            case 2 -> 1.93;
            case 3 -> 2.90;
            case 4 -> 3.87;
            case 5 -> 4.84;
            case 6 -> 5.8;
            case 7 -> 6.77;
            case 8 -> 7.74;
            default -> throw new IllegalArgumentException("Invalid value for year!");
        };
    }
}
