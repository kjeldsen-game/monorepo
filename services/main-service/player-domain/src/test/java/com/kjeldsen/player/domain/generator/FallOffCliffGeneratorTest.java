package com.kjeldsen.player.domain.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class FallOffCliffGeneratorTest {

    @Test
    @DisplayName("Should throw an error when age is invalid")
    public void should_throw_an_error_when_age_invalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            FallOffCliffGenerator.checkIfFallOffCliffHappened(25);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            FallOffCliffGenerator.checkIfFallOffCliffHappened(40);
        });
    }

    @Test
    @DisplayName("Should return true if random is lower")
    public void should_return_true_if_random_is_lower() {
        try (MockedStatic<RandomGenerator> staticMock = mockStatic(RandomGenerator.class)) {
            staticMock.when(RandomGenerator::random).thenReturn(0.4);
            assertTrue(FallOffCliffGenerator.checkIfFallOffCliffHappened(32));
        }
    }

    @Test
    @DisplayName("Should return false if random is greater")
    public void should_return_false_if_random_is_greater() {
        try (MockedStatic<RandomGenerator> staticMock = mockStatic(RandomGenerator.class)) {
            staticMock.when(RandomGenerator::random).thenReturn(0.51);
            assertFalse(FallOffCliffGenerator.checkIfFallOffCliffHappened(32));
        }
    }

}
