package com.kjeldsen.match.domain.generator;

import lombok.AllArgsConstructor;

import java.util.Random;

public class RandomGenerator {

    private static final Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
