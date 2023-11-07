package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerPositionTendencyTest {
    @Nested
    @DisplayName("Get random skill based on tendency should")
    class GetRandomSkillBasedOnTendencyShould {
        @Test
        @DisplayName("return a random skill based on the tendency")
        public void return_a_random_skill_based_on_the_tendency() {
            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getRandomSkillBasedOnTendency(Collections.emptySet())).isNotNull();
            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getRandomSkillBasedOnTendency(Collections.emptySet())).isNotNull();
        }
    }

    @Nested
    @DisplayName("Default tendencies by position should")
    class DefaultTendenciesByPositionShould {
        @Test
        @DisplayName("have the correct skill tendencies")
        public void have_the_correct_skill_tendencies() {

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(1, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0),
                    PlayerSkill.PASSING, new PlayerSkills(2, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(4, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(8, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(8, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(1, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0),
                    PlayerSkill.PASSING, new PlayerSkills(2, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(6, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(5, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(5, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(2, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0),
                    PlayerSkill.PASSING, new PlayerSkills(4, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(8, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(8, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(4, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(4, 0),
                    PlayerSkill.PASSING, new PlayerSkills(5, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(6, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(2, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(3, 0),
                    PlayerSkill.PASSING, new PlayerSkills(6, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(6, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(1, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(6, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(6, 0),
                    PlayerSkill.PASSING, new PlayerSkills(8, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(4, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(4, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(3, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(8, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(8, 0),
                    PlayerSkill.PASSING, new PlayerSkills(6, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(1, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(7, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(7, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(7, 0),
                    PlayerSkill.PASSING, new PlayerSkills(3, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(3, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(1, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                    PlayerSkill.SCORING, new PlayerSkills(5, 0),
                    PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(5, 0),
                    PlayerSkill.BALL_CONTROL, new PlayerSkills(5, 0),
                    PlayerSkill.PASSING, new PlayerSkills(3, 0),
                    PlayerSkill.AERIAL, new PlayerSkills(7, 0),
                    PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0),
                    PlayerSkill.TACKLING, new PlayerSkills(1, 0),
                    PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                    PlayerSkill.REFLEXES, new PlayerSkills(5, 0),
                    PlayerSkill.GOALKEEPER_POSITIONING, new PlayerSkills(4, 0),
                    PlayerSkill.INTERCEPTIONS, new PlayerSkills(2, 0),
                    PlayerSkill.CONTROL, new PlayerSkills(2, 0),
                    PlayerSkill.ORGANIZATION, new PlayerSkills(2, 0),
                    PlayerSkill.ONE_ON_ONE, new PlayerSkills(4, 0)
                ));
        }

        @Test
        @DisplayName("have is default set to true")
        public void have_is_default_set_to_true() {

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.isDefault()).isTrue();

            assertThat(PlayerPositionTendency.builder()
                .position(PlayerPosition.CENTRE_BACK)
                .tendencies(Map.of()).build()
                .isDefault());
        }
    }
}
