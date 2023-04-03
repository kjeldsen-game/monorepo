package com.kjeldsen.player.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointsGenerator {

    public static int generatePointsRise(int currentDay) {
        double probability = probabilityRiseBasedOnCurrentDay(currentDay);
        double randomProbabilityRise = RandomGenerator.random();
        return randomProbabilityRise <= probability ?
            generatePoints() :
            0;
    }

    public static double probabilityRiseBasedOnCurrentDay(int currentDay) {
       return  switch (currentDay) {
           case 1 -> 7.14;
           case 2 -> 14.29;
           case 3 -> 21.43;
           case 4 -> 100; // TODO update followings :D
           case 5 -> 100;
           case 6 -> 100;
           case 7 -> 100;
           case 8 -> 100;
           case 9 -> 100;
           case 10 -> 100;
           case 11 -> 100;
           case 12 -> 100;
           case 13 -> 100;
           case 14-> 100;
           default -> throw new IllegalStateException("Unexpected value: " + currentDay);
       };
    }

    public static int generatePoints(float probability, int points) {
        return (int) (probability * points) / 100;
    }

    // TODO Dali 50% for decline generatePoints -> generatePointsBloom and create generatePointsDecline
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
}
