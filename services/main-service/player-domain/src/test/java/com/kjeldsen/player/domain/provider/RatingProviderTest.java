package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.factories.PlayerSkillsFactory;
import com.kjeldsen.player.domain.utils.RatingUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatingProviderTest {

    @Test
    @DisplayName("Should throw error when skills of player are not defined")
    void should_throw_error_when_skills_of_player_are_not_defined() {
        Player player = Player.builder().position(PlayerPosition.FORWARD).actualSkills(null).build();
        assertThrows(IllegalArgumentException.class, () -> {
            RatingProvider.getRating(player, RatingUtils.getRatingMapForPosition(player.getPosition()));
        });
    }

    @Test
    @DisplayName("Should throw error when size of rating map is not same as skill size")
    void should_throw_error_when_size_of_rating_map_is_not_same() {
        Map<PlayerSkill, PlayerSkills> skills = new HashMap<>();
        skills.put(PlayerSkill.SCORING, PlayerSkills.builder().build());
        Player player = Player.builder().position(PlayerPosition.FORWARD).actualSkills(skills).build();
        assertThrows(IllegalArgumentException.class, () -> {
            RatingProvider.getRating(player, RatingUtils.getRatingMapForPosition(player.getPosition()));
        });
    }

    @Test
    @DisplayName("Should calculate the rating of the player")
    void should_calculate_rating_of_player() {
        Player player = Player.builder().position(PlayerPosition.FORWARD).actualSkills(PlayerSkillsFactory
            .createNonGoalkeeperSkills(PlayerPosition.FORWARD)).build();
        player.getActualSkills().forEach((key, skill) -> {
            skill.setPotential(10);
            skill.setActual(10);
        });
        RatingProvider.getRating(player, RatingUtils.getRatingMapForPosition(player.getPosition()));
        assertThat(player.getRating().getActual()).isEqualTo(10);
        assertThat(player.getRating().getPotential()).isEqualTo(10);
    }
}