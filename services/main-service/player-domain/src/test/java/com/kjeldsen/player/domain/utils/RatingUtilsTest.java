package com.kjeldsen.player.domain.utils;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatingUtilsTest {

    @DisplayName("Should throw an error when weight and skill array lengths mismatch")
    @ParameterizedTest
    @MethodSource("invalidRatingMapInputs")
    void should_throw_an_error_for_invalid_rating_map_inputs(Double[] weights, PlayerSkill[] skills) {
        assertThrows(IllegalArgumentException.class, () -> {
            if (skills == null) {
                RatingUtils.createRatingMap(weights);
            } else {
                RatingUtils.createRatingMap(weights, skills);
            }
        });
    }

    private static Stream<Arguments> invalidRatingMapInputs() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(
                new Double[]{1.0},
                null
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                new Double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                null
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                new Double[]{1.0},
                new PlayerSkill[]{PlayerSkill.SCORING, PlayerSkill.PASSING}
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                new Double[]{1.0, 10.0},
                new PlayerSkill[]{PlayerSkill.PASSING}
            )
        );
    }

    @Test
    @DisplayName("Should create a default map")
    void should_create_a_custom_map() {
        var map = RatingUtils.createRatingMap(new Double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        assertNotNull(map);
        assertFalse(map.isEmpty(), "Map should not be empty");
        assertTrue(
            map.values().stream().allMatch(v -> v.equals(1.0)));
    }

    @Test
    @DisplayName("Should create a custom map")
    void should_create_a_custom_map_with_weights() {
        var map = RatingUtils.createRatingMap(new Double[]{1.0}, new PlayerSkill[]{PlayerSkill.SCORING});
        assertNotNull(map);
        assertThat(map.entrySet())
            .allSatisfy(entry -> {
                assertThat(entry.getKey()).isEqualTo(PlayerSkill.SCORING);
                assertThat(entry.getValue()).isEqualTo(1.0);
            });
    }

    @ParameterizedTest
    @NullSource
    void getRatingMapForPosition_shouldThrowForNull(PlayerPosition position) {
        assertThrows(NullPointerException.class, () ->
            RatingUtils.getRatingMapForPosition(position)
        );
    }

    static Stream<PlayerPosition> invalidPositions() {
        return Stream.of(PlayerPosition.AERIAL_CENTRE_BACK, PlayerPosition.AERIAL_FORWARD, PlayerPosition.AERIAL_STRIKER, PlayerPosition.SWEEPER);
    }


    @ParameterizedTest
    @MethodSource("invalidPositions")
    void getRatingMapForPosition_shouldThrowForInvalid(PlayerPosition position) {
        assertThrows(IllegalArgumentException.class, () ->
            RatingUtils.getRatingMapForPosition(position)
        );
    }
}