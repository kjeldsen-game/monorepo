package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerActualSkillsTest {

    @Nested
    @DisplayName("AddAbilityPoints should")
    class AddAbilityPoints {
        @Test
        void increase_the_amount_points_of_a_specified_ability() {
            PlayerActualSkills abilities = PlayerActualSkills.of(PlayerPosition.FORWARD);
            abilities.addAbilityPoints(PlayerSkill.SCORE, 5);
            assertThat(abilities.getAbilityPoints(PlayerSkill.SCORE)).isEqualTo(55);
        }
    }
}
