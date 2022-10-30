package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.kjeldsen.player.domain.PlayerSkills.PlayerSkill;
import static org.assertj.core.api.Assertions.assertThat;

class PlayerSkillsTest {

    @Nested
    @DisplayName("AddAbilityPoints should")
    class AddAbilityPoints {
        @Test
        void increase_the_amount_points_of_a_specified_ability() {
            PlayerSkills abilities = PlayerSkills.of(PlayerPosition.FORWARD);
            abilities.addAbilityPoints(PlayerSkill.SC, 5);
            assertThat(abilities.getAbilityPoints(PlayerSkill.SC)).isEqualTo(55);
        }
    }
}