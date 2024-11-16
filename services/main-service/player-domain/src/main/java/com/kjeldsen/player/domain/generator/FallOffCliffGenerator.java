package com.kjeldsen.player.domain.generator;

public class FallOffCliffGenerator {

    public static Boolean checkIfFallOffCliffHappened(Integer age) {
        int probability = getProbability(age);
        double randomProbability = RandomGenerator.random() * 100;
        return randomProbability <= probability;
    }

    private static int getProbability(int age) {
        return switch (age) {
            case 30 -> 15;
            case 31 -> 35;
            case 32 -> 50;
            case 33 -> 75;
            case 34 -> 100;
            default -> throw new IllegalArgumentException("Invalid age: " + age);
        };
    }
}
