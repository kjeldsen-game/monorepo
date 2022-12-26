package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerActualSkillsTest {

    @Nested
    @DisplayName("Generate should")
    class GenerateShould {
        @Test
        @DisplayName("create an instance with the given total points distributed in the actual skills of the given position")
        public void create_an_instance_with_the_given_total_points_distributed_in_the_actual_skills_of_the_given_position() {
            PlayerActualSkills actual = PlayerActualSkills.generate(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES, 200);
            assertThat(actual.getTotalPoints()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Add skill points should")
    class AddSkillPointsShould {
        @Test
        @DisplayName("increase the amount points of a specified skill")
        void increase_the_amount_points_of_a_specified_skill() {
            PlayerActualSkills actualSkills = PlayerActualSkills.of(new HashMap<>(Map.of(PlayerSkill.SCORE, 50)));
            actualSkills.addSkillPoints(PlayerSkill.SCORE, 5);
            assertThat(actualSkills.getSkillPoints(PlayerSkill.SCORE)).isEqualTo(55);
        }

        @Test
        @DisplayName("increase the amount points of a specified skill to the maximum")
        public void increase_the_amount_points_of_a_specified_skill_to_the_maximum() {
            PlayerActualSkills actualSkills = PlayerActualSkills.of(new HashMap<>(Map.of(PlayerSkill.SCORE, 98)));
            actualSkills.addSkillPoints(PlayerSkill.SCORE, 5);
            assertThat(actualSkills.getSkillPoints(PlayerSkill.SCORE)).isEqualTo(100);
        }

        @Test
        @DisplayName("not increase the amount points of a specified skill above the maximum")
        public void not_increase_the_amount_points_of_a_specified_skill_above_the_maximum() {
            PlayerActualSkills actualSkills = PlayerActualSkills.of(new HashMap<>(Map.of(PlayerSkill.SCORE, 100)));
            actualSkills.addSkillPoints(PlayerSkill.SCORE, 5);
            assertThat(actualSkills.getSkillPoints(PlayerSkill.SCORE)).isEqualTo(100);
        }
    }
}
