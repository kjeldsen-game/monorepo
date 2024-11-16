package com.kjeldsen.player.domain.generator;

import java.util.Random;

public class BloomPhaseGenerator {

    private static final Random random = new Random();

    public static int generateBloomPhaseYear() {

        int randomValue = random.nextInt(4);
        return switch (randomValue) {
            case 0 -> 18;
            case 1 -> 19;
            case 2 -> 20;
            case 3 -> 21;
            default -> throw new IllegalStateException("Unexpected value: " + randomValue);
        };
    }
}
