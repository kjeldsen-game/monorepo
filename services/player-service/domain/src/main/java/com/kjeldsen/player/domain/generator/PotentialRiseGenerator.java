package com.kjeldsen.player.domain.generator;

public class PotentialRiseGenerator {
    public static int generatePotentialRaise() {

        double randomProbabilityPorcentage = RandomGenerator.random() * 100;

        if (randomProbabilityPorcentage <= 2) {
            return 4;
        } else if (randomProbabilityPorcentage >= 3 && randomProbabilityPorcentage <= 4) {
            return 3;
        } else if (randomProbabilityPorcentage >= 5 && randomProbabilityPorcentage <= 6) {
            return 2;
        } else if (randomProbabilityPorcentage >= 7 && randomProbabilityPorcentage <= 8) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int generateRaiseProbability() {
        int probability = 8;
        double randomProbabilityRise = RandomGenerator.random() * 100;

        if (randomProbabilityRise <= probability) {
            return generatePotentialRaise();
        } else {
            return 0;
        }
    }
}
