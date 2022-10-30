package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerAgeTest {

    @Nested
    @DisplayName("Of should")
    class Of {
        @Test
        void throw_an_exception_when_value_is_less_than_15() {
            int age = 14;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> PlayerAge.of(age));
            assertThat(exception.getMessage()).isEqualTo("Age must be between 15 and 33");
        }

        @Test
        void return_age_when_value_is_between_15_and_33() {
            int age = 15;
            PlayerAge actual = PlayerAge.of(age);
            assertThat(actual.value()).isEqualTo(15);
        }

        @Test
        void throw_an_exception_when_value_is_greater_than_33() {
            int age = 35;
            Exception exception = assertThrows(IllegalArgumentException.class, () -> PlayerAge.of(age));
            assertThat(exception.getMessage()).isEqualTo("Age must be between 15 and 33");
        }
    }

}