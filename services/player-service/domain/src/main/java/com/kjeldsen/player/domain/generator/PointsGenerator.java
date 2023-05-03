package com.kjeldsen.player.domain.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointsGenerator {

    public static int generatePointsRise(int currentDay) {
        double probability = probabilityRiseBasedOnCurrentDay(currentDay);
        double randomProbabilityRise = RandomGenerator.random() * 100;

        if (randomProbabilityRise <= probability) {
            return generatePoints();
        } else {
            return 0;
        }
    }

    public static double probabilityRiseBasedOnCurrentDay(int currentDay) {
        return switch (currentDay) {
            case 1 -> 7.14;
            case 2 -> 14.29;
            case 3 -> 21.43;
            case 4 -> 28.57;
            case 5 -> 35.71;
            case 6 -> 42.86;
            case 7 -> 50;
            case 8 -> 57.14;
            case 9 -> 64.29;
            case 10 -> 71.43;
            case 11 -> 78.57;
            case 12 -> 85.71;
            case 13 -> 92.86;
            case 14 -> 100;
            default -> throw new IllegalStateException("Unexpected value (from 1 - 14 days): " + currentDay);
        };
    }

    public static int generatePoints() {

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

    public static int generatePointsBloom(float probability, int points) {
        return (int) ((probability * points) / 100);
    }

    public static int generateDecreasePoints(float probability, int points) {
        return (int) ((probability * points) / 100);
    }

}
