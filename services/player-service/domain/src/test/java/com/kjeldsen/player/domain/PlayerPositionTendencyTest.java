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
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 1,
                    PlayerSkill.BALL_CONTROL, 2,
                    PlayerSkill.PASSING, 2,
                    PlayerSkill.AERIAL, 4,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 8,
                    PlayerSkill.DEFENSE_POSITION, 8
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 1,
                    PlayerSkill.BALL_CONTROL, 2,
                    PlayerSkill.PASSING, 2,
                    PlayerSkill.AERIAL, 6,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 5,
                    PlayerSkill.DEFENSE_POSITION, 5
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 2,
                    PlayerSkill.BALL_CONTROL, 2,
                    PlayerSkill.PASSING, 4,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 8,
                    PlayerSkill.DEFENSE_POSITION, 8
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 4,
                    PlayerSkill.BALL_CONTROL, 4,
                    PlayerSkill.PASSING, 5,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 6,
                    PlayerSkill.DEFENSE_POSITION, 6
                ));

            assertThat(PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 2,
                    PlayerSkill.BALL_CONTROL, 3,
                    PlayerSkill.PASSING, 6,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 6,
                    PlayerSkill.DEFENSE_POSITION, 6
                ));

            assertThat(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 6,
                    PlayerSkill.BALL_CONTROL, 6,
                    PlayerSkill.PASSING, 8,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 4,
                    PlayerSkill.DEFENSE_POSITION, 4
                ));

            assertThat(PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 3,
                    PlayerSkill.OFFENSIVE_POSITION, 8,
                    PlayerSkill.BALL_CONTROL, 8,
                    PlayerSkill.PASSING, 6,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 1,
                    PlayerSkill.DEFENSE_POSITION, 1
                ));

            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 7,
                    PlayerSkill.OFFENSIVE_POSITION, 7,
                    PlayerSkill.BALL_CONTROL, 7,
                    PlayerSkill.PASSING, 3,
                    PlayerSkill.AERIAL, 3,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 1,
                    PlayerSkill.DEFENSE_POSITION, 1
                ));

            assertThat(PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.SCORE, 5,
                    PlayerSkill.OFFENSIVE_POSITION, 5,
                    PlayerSkill.BALL_CONTROL, 5,
                    PlayerSkill.PASSING, 3,
                    PlayerSkill.AERIAL, 7,
                    PlayerSkill.CO, 4,
                    PlayerSkill.TACKLING, 1,
                    PlayerSkill.DEFENSE_POSITION, 1
                ));

            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(
                    PlayerSkill.REFLEXES, 5,
                    PlayerSkill.POSITIONING, 4,
                    PlayerSkill.INTERCEPTIONS, 2,
                    PlayerSkill.CONTROL, 2,
                    PlayerSkill.ORGANIZATION, 2,
                    PlayerSkill.ONE_ON_ONE, 4
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
