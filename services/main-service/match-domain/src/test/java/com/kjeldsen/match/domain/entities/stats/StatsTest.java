package com.kjeldsen.match.domain.entities.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    @DisplayName("Should generate the Stats id")
    void should_generate_stats_id() {
        Stats.StatsId statsId = Stats.StatsId.generate();
        assertNotNull(statsId);
    }

    @Test
    @DisplayName("Should test 2 ids are not same")
    void should_not_test_ids_are_same() {
        Stats.StatsId statsId = Stats.StatsId.generate();
        Stats.StatsId statsId2 = Stats.StatsId.generate();

        assertNotEquals(statsId, statsId2);
    }

    @Test
    @DisplayName("Should create an stats id from string value")
    void should_create_stats_id_from_string_value() {
        Stats.StatsId statsId = Stats.StatsId.of("exampleStatsId");
        assertThat(statsId).isNotNull();
        assertThat(statsId.value()).isEqualTo("exampleStatsId");
    }

    @Test
    @DisplayName("String result of toString should contain parts")
    void should_contain_parts() {
        Stats.StatsId statsId = Stats.StatsId.of("exampleStatsId");
        assertThat(statsId.toString()).contains("StatsId[value=exampleStatsId]");
    }

    @Test
    @DisplayName("Should create object with al args in constructor")
    void should_create_object_with_al_args_in_constructor() {
        Stats.StatsId statsId = new Stats.StatsId("exampleStatsId");
        assertThat(statsId).isNotNull();
        assertThat(statsId.value()).isEqualTo("exampleStatsId");
    }
}