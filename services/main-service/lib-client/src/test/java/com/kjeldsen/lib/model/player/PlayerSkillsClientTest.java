package com.kjeldsen.lib.model.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlayerSkillsClientTest {

    @Test
    @DisplayName("Should test if to string contains parts")
    void should_test_to_string() {
        PlayerSkillsClient skills = PlayerSkillsClient.builder()
            .potential(10).actual(2).playerSkillRelevance("CORE").build();
        assertThat(skills.toString()).contains("potential=10");
    }

    @Test
    @DisplayName("Should build a player skill")
    void should_build_player_skills_client() {
        PlayerSkillsClient built = PlayerSkillsClient.builder().build();
        assertThat(built).isNotNull();
    }
    @Test
    @DisplayName("Should return and set value")
    void should_return_and_set_value() {
        PlayerSkillsClient built= PlayerSkillsClient.builder().build();
        built.setActual(5);
        assertThat(built.getActual()).isEqualTo(5);
    }
}