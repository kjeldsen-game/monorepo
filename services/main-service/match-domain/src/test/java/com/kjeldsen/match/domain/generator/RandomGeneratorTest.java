package com.kjeldsen.match.domain.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class RandomGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {24, 20, 30})
    @DisplayName("Should generate numbers in desired range integers")
    void should_generate_numbers_in_desired_range_integers(int maxRange) {
        int result = RandomGenerator.randomInt(0, maxRange);
        assertThat(result).isBetween(0, maxRange);
    }

    @ParameterizedTest
    @ValueSource(doubles = {44.43, 23.5, 65.5})
    @DisplayName("Should generate numbers in desired range integers")
    void should_generate_numbers_in_desired_range_double(double maxRange) {
        double result = RandomGenerator.randomDouble(4.2, maxRange);
        assertThat(result).isBetween(4.2, maxRange);
    }
}