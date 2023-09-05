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
            assertThat(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.getRandomSkillBasedOnTendency(Collections.emptySet())).isNotNull();
            assertThat(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES.getRandomSkillBasedOnTendency(Collections.emptySet())).isNotNull();
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
            assertThat(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(PlayerSkill.TACKLING, 5,
                    PlayerSkill.DEFENSE_POSITION, 5,
                    PlayerSkill.CO, 2,
                    PlayerSkill.SCORE, 1,
                    PlayerSkill.OFFENSIVE_POSITION, 1,
                    PlayerSkill.BALL_CONTROL, 1,
                    PlayerSkill.PASSING, 1
                ));
            assertThat(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(PlayerSkill.PASSING, 4,
                    PlayerSkill.BALL_CONTROL, 3,
                    PlayerSkill.OFFENSIVE_POSITION, 3,
                    PlayerSkill.CO, 2,
                    PlayerSkill.TACKLING, 2,
                    PlayerSkill.DEFENSE_POSITION, 2,
                    PlayerSkill.SCORE, 1
                ));
            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(PlayerSkill.SCORE, 5,
                    PlayerSkill.OFFENSIVE_POSITION, 4,
                    PlayerSkill.BALL_CONTROL, 4,
                    PlayerSkill.PASSING, 2,
                    PlayerSkill.CO, 2,
                    PlayerSkill.TACKLING, 1,
                    PlayerSkill.DEFENSE_POSITION, 1
                ));
            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.getTendencies())
                .isEqualTo(Map.of(PlayerSkill.REFLEXES, 5,
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
            assertThat(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES.isDefault()).isTrue();
            assertThat(PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES.isDefault()).isTrue();

            assertThat(PlayerPositionTendency.builder()
                .position(PlayerPosition.DEFENDER)
                .tendencies(Map.of()).build()
                .isDefault()
            ).isFalse();
        }
    }

}
