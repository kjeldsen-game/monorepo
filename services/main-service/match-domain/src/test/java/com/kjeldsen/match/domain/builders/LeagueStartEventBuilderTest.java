package com.kjeldsen.match.domain.builders;

import com.kjeldsen.lib.events.NotificationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LeagueStartEventBuilderTest {

    @Test
    @DisplayName("Should create a notification about league start")
    void should_create_notification_about_placed_bid() {
        NotificationEvent n = LeagueStartEventBuilder.build(List.of("teamid", "teamid2", "teamid3"));
        assertNotNull(n);
        assertThat(n.getTeamIds()).hasSize(3);
        assertThat(n.getTeamIds()).isNotEmpty();
        assertThat(n.getType()).isEqualTo(NotificationEvent.NotificationEventType.LEAGUE_START);
        assertThat(n.getPayload()).isNull();
    }
}