package com.kjeldsen.player.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointsGenerator {

//    public static int
//    generatePointsRise(int currentDay) {
//
//        return generatePointsRise(currentDay, probability);
//    }

    public static int generatePointsRise(int currentDay) {
        float probability = 1 / 14;
        double randomProbabilityRise = RandomGenerator.random();

        double probabilityRaise = probability * currentDay;

        int risePoints = 0;

        if (randomProbabilityRise <= probabilityRaise) {
            risePoints = generatePoints();
        }

        return risePoints;
    }

    public static int generatePoints(float probability, int points) {

        return (int) (probability * points) / 100;
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
}
