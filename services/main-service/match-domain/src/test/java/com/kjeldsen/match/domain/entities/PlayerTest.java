package com.kjeldsen.match.domain.entities;

import com.kjeldsen.match.common.RandomHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    static Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = RandomHelper.genPlayer(Team.builder().build());
    }

    @Test
    @DisplayName("Should create player with simplified data")
    void should_create_player_with_simplified_data() {
        Player simplifiedPlayer = testPlayer.getSimplifiedPlayerData();
        assertNotNull(simplifiedPlayer);
        assertEquals(testPlayer.getId(), simplifiedPlayer.getId());
        assertEquals(testPlayer.getName(), simplifiedPlayer.getName());
        assertNull(simplifiedPlayer.getSkills());
    }

    @Test
    @DisplayName("Should create a deep copy of player")
    void should_create_a_deep_copy_of_player() {
        testPlayer.setName("TestPlayer");
        Player deepCopy = testPlayer.deepCopy();

        assertEquals(testPlayer, deepCopy);
        deepCopy.setName("Example name");
        assertNotEquals(testPlayer, deepCopy);
        assertEquals("Example name", deepCopy.getName());
        assertEquals("TestPlayer", testPlayer.getName());

    }
}