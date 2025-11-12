package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    @DisplayName("Should assign actual and potential ratings")
    void should_assign_actual_and_potential_ratings() {
        Rating rating = Rating.builder().build();
        rating.setRatings(Map.of("actual", 1.0, "potential", 1.0));
        assertEquals(1.0, rating.getActual());
    }
}