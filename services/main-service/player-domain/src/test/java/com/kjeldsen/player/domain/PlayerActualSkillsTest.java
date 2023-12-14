package com.kjeldsen.player.domain;

import com.kjeldsen.player.domain.provider.PlayerProvider;
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
        void create_an_instance_with_the_given_total_points_distributed_in_the_actual_skills_of_the_given_position() {
            Player player = Player.builder()
                    .actualSkills(PlayerProvider.skillsBasedOnTendency(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES, 200))
                    .build();

            assertThat(player.getActualSkills().values()
                    .stream()
                    .mapToInt(PlayerSkills::getActual)
                    .sum())
                    .isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Add skill points should")
    class AddSkillPointsShould {
        @Test
        @DisplayName("increase the amount points of a specified skill")
        void increase_the_amount_points_of_a_specified_skill() {

            PlayerSkills skillPoints = new PlayerSkills(50, 0, PlayerSkillRelevance.RESIDUAL);

            Player player = Player.builder()
                    .position(PlayerPosition.CENTRE_BACK)
                    .actualSkills(new HashMap<>(Map.of(PlayerSkill.SCORING, skillPoints)))
                    .build();
            player.getActualSkills().get(PlayerSkill.SCORING).increaseActualPoints(5);
            assertThat(player.getActualSkillPoints(PlayerSkill.SCORING)).isEqualTo(55);
        }

        @Test
        @DisplayName("increase the amount points of a specified skill to the maximum")
        void increase_the_amount_points_of_a_specified_skill_to_the_maximum() {

            PlayerSkills skillPoints = new PlayerSkills(98, 0, PlayerSkillRelevance.RESIDUAL);
            Player player = Player.builder()
                    .position(PlayerPosition.CENTRE_BACK)
                    .actualSkills(new HashMap<>(Map.of(PlayerSkill.SCORING, skillPoints)))
                    .build();
            player.getActualSkills().get(PlayerSkill.SCORING).increaseActualPoints(5);
            assertThat(player.getActualSkillPoints(PlayerSkill.SCORING)).isEqualTo(100);
        }

        @Test
        @DisplayName("not increase the amount points of a specified skill above the maximum")
        void not_increase_the_amount_points_of_a_specified_skill_above_the_maximum() {
            PlayerSkills skillPoints = new PlayerSkills(100, 0, PlayerSkillRelevance.RESIDUAL);
            Player player = Player.builder()
                    .position(PlayerPosition.CENTRE_BACK)
                    .actualSkills(new HashMap<>(Map.of(PlayerSkill.SCORING, skillPoints)))
                    .build();
            player.getActualSkills().get(PlayerSkill.SCORING).increaseActualPoints(5);
            assertThat(player.getActualSkillPoints(PlayerSkill.SCORING)).isEqualTo(100);
        }
    }
}
