package com.kjeldsen.player.domain.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointsGenerator {

    private static final Float increaseMaxPorcentage = 60f;
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
            case 1 -> 2.03;
            case 2 -> 2.73;
            case 3 -> 3.69;
            case 4 -> 4.98;
            case 5 -> 6.73;
            case 6 -> 9.08;
            case 7 -> 12.26;
            case 8 -> 16.55;
            case 9 -> 22.34;
            case 10 -> 30.16;
            case 11 -> 40.72;
            case 12 -> 54.97;
            case 13 -> 74.20;
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


    // Generator of the Potencial points -- Phase I -- 60% more than the first ones points

    public static int generatePotencialPoints(int actualPoints){
       return (int) (increaseMaxPorcentage * actualPoints);
    }
}
