package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerPositionTest {
    @Nested
    @DisplayName("Get random skill based on tendency should")
    class GetRandomSkillBasedOnTendencyShould {
        @Test
        @DisplayName("return a random skill based on the tendency")
        public void return_a_random_skill_based_on_the_tendency() {
            assertThat(PlayerPosition.FORWARD.getRandomSkillBasedOnTendency(new HashSet<>())).isNotNull();
            assertThat(PlayerPosition.MIDDLE.getRandomSkillBasedOnTendency(new HashSet<>())).isNotNull();
            assertThat(PlayerPosition.DEFENDER.getRandomSkillBasedOnTendency(new HashSet<>())).isNotNull();
        }
    }
}
