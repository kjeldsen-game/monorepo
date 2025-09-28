package com.kjeldsen.match.domain.builders;

import com.kjeldsen.lib.events.NotificationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MatchEndNotificationEventBuilderTest {

    @Test
    @DisplayName("Should throw error when match id is missing")
    void should_throw_error_when_match_id_is_missing() {
        assertThrows(IllegalArgumentException.class, () -> {
            MatchEndNotificationEventBuilder.build(List.of("id"), null);
        });
    }

    @Test
    @DisplayName("Should create a notification about match end")
    void should_create_notification_about_placed_bid() throws IllegalArgumentException {
        NotificationEvent n = MatchEndNotificationEventBuilder.build(List.of("teamid"), "matchId");
        assertNotNull(n);
        assertThat(n.getTeamIds()).isNotEmpty();
        assertThat(n.getType()).isEqualTo(NotificationEvent.NotificationEventType.MATCH_END);
        assertThat(n.getPayload().get("matchId")).isEqualTo("matchId");
    }
}