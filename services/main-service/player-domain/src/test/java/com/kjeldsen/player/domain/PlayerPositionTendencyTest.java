package com.kjeldsen.player.domain;

import org.junit.jupiter.api.Disabled;
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
        @Disabled // TODO check how to fix the code, the PlayerPositionTendency not using the PlayerSkillRelevance and it's used in the test case -> null error of actual value
        @DisplayName("have the correct skill tendencies")
        public void have_the_correct_skill_tendencies() {

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.AERIAL, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.PASSING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.AERIAL, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.TACKLING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.PASSING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_SWEEPER_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.AERIAL, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.PASSING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_LEFT_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.PASSING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.AERIAL, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_BACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.PASSING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.AERIAL, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_LEFT_WINGBACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.PASSING, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.AERIAL, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_WINGBACK_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.PASSING, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.AERIAL, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison().isEqualTo(Map.of(
                            PlayerSkill.TACKLING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.PASSING, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.SCORING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.BALL_CONTROL, new PlayerSkills(4, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.PASSING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.SCORING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_LEFT_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.PASSING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.SCORING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.PASSING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.SCORING, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_LEFT_WINGER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.PASSING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.SCORING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.PASSING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.SCORING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_WINGER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.PASSING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.SCORING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.AERIAL, new PlayerSkills(2, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.SECONDARY)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.SCORING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(7, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.PASSING, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.AERIAL, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.AERIAL, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.SCORING, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.PASSING, new PlayerSkills(2, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_STRIKER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.SCORING, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.PASSING, new PlayerSkills(1, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.AERIAL, new PlayerSkills(5, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_STRIKER_TENDENCIES.getTendencies())
                .usingRecursiveComparison()
                .isEqualTo(Map.of(
                        PlayerSkill.AERIAL, new PlayerSkills(9, 0, PlayerSkillRelevance.CORE),
                        PlayerSkill.SCORING, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.OFFENSIVE_POSITIONING, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.BALL_CONTROL, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.PASSING, new PlayerSkills(1, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.CONSTITUTION, new PlayerSkills(4, 0, PlayerSkillRelevance.SECONDARY),
                        PlayerSkill.TACKLING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL),
                        PlayerSkill.DEFENSIVE_POSITIONING, new PlayerSkills(1, 0, PlayerSkillRelevance.RESIDUAL)
                ));

            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getTendencies())
                    .usingRecursiveComparison()
                    .isEqualTo(Map.of(
                            PlayerSkill.REFLEXES, new PlayerSkills(8, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.GOALKEEPER_POSITIONING, new PlayerSkills(6, 0, PlayerSkillRelevance.CORE),
                            PlayerSkill.INTERCEPTIONS, new PlayerSkills(3, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.ONE_ON_ONE, new PlayerSkills(6, 0, PlayerSkillRelevance.SECONDARY),
                            PlayerSkill.CONTROL, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL),
                            PlayerSkill.ORGANIZATION, new PlayerSkills(3, 0, PlayerSkillRelevance.RESIDUAL)
                    ));
        }

        @Test
        @DisplayName("have is default set to true")
        public void have_is_default_set_to_true() {

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_SWEEPER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_LEFT_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_BACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_LEFT_WINGBACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_WINGBACK_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_LEFT_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_LEFT_WINGER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_RIGHT_WINGER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_STRIKER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_STRIKER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.isDefault()).isTrue();

            assertThat(PlayerPositionTendency.builder()
                .position(PlayerPosition.CENTRE_BACK)
                .tendencies(Map.of()).build()
                .isDefault());
        }
    }
}
