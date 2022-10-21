package com.kjeldsen.player.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.kjeldsen.player.domain.PlayerAbilities.PlayerAbility;
import static org.assertj.core.api.Assertions.assertThat;

class PlayerAbilitiesTest {

    @Nested
    @DisplayName("AddAbilityPoints should")
    class AddAbilityPoints {
        @Test
        void increase_the_amount_points_of_a_specified_ability() {
            PlayerAbilities abilities = PlayerAbilities.of(PlayerPosition.FORWARD);
            abilities.addAbilityPoints(PlayerAbility.SPEED, 5);
            assertThat(abilities.getAbilityPoints(PlayerAbility.SPEED)).isEqualTo(55);
        }
    }
}