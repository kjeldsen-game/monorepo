package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AgeTest {

    @Nested
    @DisplayName("Of should")
    class Of {
        @Test
        void return_null_when_value_is_less_than_15() {
            // given
            int age = 14;

            // when
            Age actual = Age.of(age);

            // then
            assertNull(actual);
        }

        @Test
        void return_age_when_value_is_between_15_and_33() {
            // given
            int age = 15;

            // when
            Age actual = Age.of(age);

            // then
            assertNotNull(actual);
            assertEquals(age, actual.value());
        }

        @Test
        void return_null_when_value_is_greater_than_33() {
            // given
            int age = 34;

            // when
            Age actual = Age.of(age);

            // then
            assertNull(actual);
        }
    }

}