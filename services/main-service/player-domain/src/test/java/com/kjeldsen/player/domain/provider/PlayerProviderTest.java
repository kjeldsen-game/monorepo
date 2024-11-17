package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerProviderTest {

    @Test
    @DisplayName("Should return null when no player skill over 30 is present")
    void should_return_null_when_no_player_skill_over_30() {
        Player testPlayer = Player.builder().build();
        testPlayer.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(32).build()));
        assertNull(PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(testPlayer)));
    }

    @Test
    @DisplayName("Should return random skill")
    void should_return_skill_random_skill() {
        Player testPlayer = Player.builder().build();
        testPlayer.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(42).potential(43).build()));
        assertEquals(PlayerSkill.AERIAL, PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(testPlayer)));
    }
}