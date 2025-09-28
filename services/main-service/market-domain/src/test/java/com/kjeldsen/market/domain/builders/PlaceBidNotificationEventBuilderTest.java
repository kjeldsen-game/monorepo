package com.kjeldsen.market.domain.builders;

import com.kjeldsen.lib.events.NotificationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceBidNotificationEventBuilderTest {

    @Test
    @DisplayName("Should throw error when playerId is not specified")
    void should_throw_error_when_playerId_is_not_specified() {
        assertThrows(IllegalArgumentException.class, () -> {
            PlaceBidNotificationEventBuilder.build(List.of("teamid"), null);
        });
    }

    @Test
    @DisplayName("Should create a notification about placed bid")
    void should_create_notification_about_placed_bid() throws IllegalArgumentException {
        NotificationEvent n = PlaceBidNotificationEventBuilder.build(List.of("teamid"), "player");
        assertNotNull(n);
        assertThat(n.getTeamIds()).isNotEmpty();
        assertThat(n.getType()).isEqualTo(NotificationEvent.NotificationEventType.AUCTION_BID);
        assertThat(n.getPayload().get("playerId")).isEqualTo("player");
    }
}