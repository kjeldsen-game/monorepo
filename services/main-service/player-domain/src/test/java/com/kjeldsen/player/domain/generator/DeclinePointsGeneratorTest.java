package com.kjeldsen.player.domain.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class DeclinePointsGeneratorTest {

    @Test
    @DisplayName("Should throw an error when age is invalid")
    public void should_throw_an_error_when_age_invalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            DeclinePointsGenerator.generateDeclinePoints(1, 9);
        }, "Invalid value for year!");
    }

    @Test
    @DisplayName("Should return points if random is lower")
    public void should_return_points_if_random_is_lower() {
        try (MockedStatic<RandomGenerator> staticMock = mockStatic(RandomGenerator.class)) {
            staticMock.when(RandomGenerator::random).thenReturn(0.4);
            assertEquals(1, DeclinePointsGenerator.generateDeclinePoints(12, 5));
        }
    }

    @Test
    @DisplayName("Should return zero if random is greater")
    public void should_return_zero_if_random_is_lower() {
        try (MockedStatic<RandomGenerator> staticMock = mockStatic(RandomGenerator.class)) {
            staticMock.when(RandomGenerator::random).thenReturn(0.4);
            // 4.84 * 1 = 4.84 < 40
            assertEquals(0, DeclinePointsGenerator.generateDeclinePoints(1, 5));
        }
    }
}
