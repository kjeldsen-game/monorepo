package com.kjeldsen.player.engine;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointsGeneratorTest {

    @ParameterizedTest
    @MethodSource(value = "testInputs")
    void generatePointsTest(double mockedRandom, int expectedPoints) {
        try (MockedStatic<RandomGenerator> randomGeneratorMockedStatic = Mockito.mockStatic(RandomGenerator.class)) {
            randomGeneratorMockedStatic.when(RandomGenerator::random).thenReturn(mockedRandom);
            assertEquals(expectedPoints, PointsGenerator.generatePoints());
        }
    }

    private static Stream<Arguments> testInputs() {
        return Stream.of(
            Arguments.of(0.00, 1),
            Arguments.of(0.35, 1),
            Arguments.of(0.36, 2),
            Arguments.of(0.70, 2),
            Arguments.of(0.71, 3),
            Arguments.of(0.85, 3),
            Arguments.of(0.86, 4),
            Arguments.of(0.95, 4),
            Arguments.of(0.96, 5),
            Arguments.of(1.00, 5));
    }
}
