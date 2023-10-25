package com.kjeldsen.match.entities;

import static com.kjeldsen.match.entities.PitchArea.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PitchAreaTest {

    @Test
    void teammateIsNearbyTrueWhenAdjacent() {
        assertTrue(LEFT_FORWARD.teammateIsNearby(LEFT_MIDFIELD));
        assertTrue(LEFT_MIDFIELD.teammateIsNearby(LEFT_FORWARD));
        assertTrue(RIGHT_FORWARD.teammateIsNearby(RIGHT_MIDFIELD));
        assertTrue(RIGHT_MIDFIELD.teammateIsNearby(RIGHT_FORWARD));
        assertTrue(CENTER_FORWARD.teammateIsNearby(CENTER_MIDFIELD));
        assertTrue(CENTER_MIDFIELD.teammateIsNearby(CENTER_FORWARD));
        assertTrue(CENTER_BACK.teammateIsNearby(CENTER_MIDFIELD));
        assertTrue(CENTER_MIDFIELD.teammateIsNearby(CENTER_BACK));
        assertTrue(CENTER_BACK.teammateIsNearby(LEFT_BACK));
        assertTrue(LEFT_BACK.teammateIsNearby(CENTER_BACK));
    }

    @Test
    void teammateIsNearbyTrueWhenInSameArea() {
        assertTrue(LEFT_BACK.teammateIsNearby(LEFT_BACK));
        assertTrue(CENTER_MIDFIELD.teammateIsNearby(CENTER_MIDFIELD));
        assertTrue(RIGHT_FORWARD.teammateIsNearby(RIGHT_FORWARD));
    }

    @Test
    void teammateIsNearbyFalseWhenNotAdjacent() {
        assertFalse(LEFT_FORWARD.teammateIsNearby(RIGHT_FORWARD));
        assertFalse(LEFT_BACK.teammateIsNearby(CENTER_FORWARD));
        assertFalse(LEFT_BACK.teammateIsNearby(RIGHT_BACK));
        assertFalse(RIGHT_FORWARD.teammateIsNearby(LEFT_FORWARD));
        assertFalse(RIGHT_BACK.teammateIsNearby(LEFT_BACK));
    }

    @Test
    void opponentIsNearbyTrueWhenAdjacent() {
        assertTrue(CENTER_MIDFIELD.opponentIsNearby(RIGHT_BACK));
        assertTrue(RIGHT_BACK.opponentIsNearby(CENTER_MIDFIELD));
        assertTrue(CENTER_FORWARD.opponentIsNearby(RIGHT_BACK));
        assertTrue(LEFT_BACK.opponentIsNearby(CENTER_FORWARD));
    }

    @Test
    void opponentIsNearbyTrueWhenInSameArea() {
        assertTrue(LEFT_BACK.opponentIsNearby(RIGHT_FORWARD));
        assertTrue(RIGHT_BACK.opponentIsNearby(LEFT_FORWARD));
        assertTrue(CENTER_FORWARD.opponentIsNearby(CENTER_BACK));
        assertTrue(CENTER_BACK.opponentIsNearby(CENTER_FORWARD));
        assertTrue(CENTER_MIDFIELD.opponentIsNearby(CENTER_MIDFIELD));
        assertTrue(CENTER_MIDFIELD.opponentIsNearby(RIGHT_MIDFIELD));

    }

    @Test
    void opponentIsNearbyFalseWhenNotAdjacent() {
        assertFalse(LEFT_FORWARD.opponentIsNearby(LEFT_BACK));
        assertFalse(LEFT_BACK.opponentIsNearby(LEFT_FORWARD));
        assertFalse(RIGHT_FORWARD.opponentIsNearby(RIGHT_BACK));
        assertFalse(RIGHT_BACK.opponentIsNearby(RIGHT_FORWARD));
        assertFalse(CENTER_FORWARD.opponentIsNearby(CENTER_FORWARD));
        assertFalse(CENTER_BACK.opponentIsNearby(CENTER_BACK));
        assertFalse(LEFT_BACK.opponentIsNearby(LEFT_BACK));
    }
}