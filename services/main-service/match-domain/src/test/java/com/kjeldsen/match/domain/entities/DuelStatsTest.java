package com.kjeldsen.match.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DuelStatsTest {

    @Test
    @DisplayName("Should create basic DuelStats object without assistance")
    void should_create_basic_duel_stats_object_without_assistance() {
        DuelStats duelStats = DuelStats.initWithoutAssistance();
        assertNotNull(duelStats);
        assertThat(duelStats.getTotal()).isEqualTo(0);
        assertThat(duelStats.getSkillPoints()).isEqualTo(0);
        assertThat(duelStats.getCarryover()).isEqualTo(0);
        assertThat(duelStats.getAssistance()).isNull();
        assertThat(duelStats.getPerformance()).isNotNull();
    }

    @Test
    @DisplayName("Should create default params of DuelStats object")
    void should_create_default_duel_stats_object() {
        DuelStats duelStats = DuelStats.initDefault();
        assertThat(duelStats.getTotal()).isEqualTo(0);
        assertThat(duelStats.getSkillPoints()).isEqualTo(0);
        assertThat(duelStats.getCarryover()).isEqualTo(0);
        assertThat(duelStats.getAssistance()).isNotNull();
        assertThat(duelStats.getPerformance()).isNotNull();
    }
}