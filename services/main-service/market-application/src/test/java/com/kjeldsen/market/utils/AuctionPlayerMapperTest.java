package com.kjeldsen.market.utils;

import com.kjeldsen.lib.model.player.PlayerAgeClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuctionPlayerMapperTest {

    @Test
    @DisplayName("Should return null when object is null")
    void should_map_player_age() {
        assertNull(AuctionPlayerMapper.INSTANCE.mapAge(null));
    }

    @Test
    @DisplayName("Should return mapped age object")
    void should_return_mapped_age_object() {
        PlayerAgeClient playerAge = PlayerAgeClient.builder().days(11.1).months(11.1).years(12).build();
        assertThat(AuctionPlayerMapper.INSTANCE.mapAge(playerAge)).usingRecursiveComparison().isEqualTo(playerAge);
    }

    @Test
    @DisplayName("Should return null mapped skills")
    void should_return_null_mapped_skills() {
        assertNull(AuctionPlayerMapper.INSTANCE.mapSkills(null));
    }
}