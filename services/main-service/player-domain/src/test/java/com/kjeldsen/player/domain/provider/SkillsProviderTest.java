package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.Rating;
import com.kjeldsen.player.domain.factories.PlayerSkillsFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SkillsProviderTest {

    @Test
    @DisplayName("Should generate skills based on the rating")
    void should_generate_skills_based_on_the_rating() {
        Player player = Player.builder()
            .actualSkills(PlayerSkillsFactory.createNonGoalkeeperSkills(PlayerPosition.FORWARD))
            .preferredPosition(PlayerPosition.FORWARD)
            .rating(Rating.builder().actual(10.0).potential(20.0).build()).build();
        SkillsProvider.provide(player);
        assertThat(player.getActualSkills()).hasSize(8);
        player.getActualSkills().values()
            .forEach(skill -> assertThat(skill.getActual()).isNotNull());
        player.getActualSkills().values()
            .forEach(skill -> assertThat(skill.getPotential()).isNotNull());
    }
}