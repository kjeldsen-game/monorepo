package com.kjeldsen.lib.builders;

import com.kjeldsen.lib.events.NotificationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;


class NotificationEventBuilderTest {

    @ParameterizedTest
    @EnumSource(NotificationEvent.NotificationEventType.class)
    @DisplayName("Should create a notification event")
    void should_create_a_notification_event(NotificationEvent.NotificationEventType type) {
        NotificationEvent event = NotificationEventBuilder.buildBaseEvent(List.of("team1"), null, "message", type);
        assertThat(event).isNotNull();
        assertThat(event.getType()).isEqualTo(type);
    }

    static Stream<List<String>> invalidTeamIdsProvider() {
        return Stream.of(
            null,
            List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTeamIdsProvider")
    @DisplayName("Should return null when teamIds are null or empty")
    void should_return_null_when_teamIds_are_invalid(List<String> teamIds) {
        NotificationEvent event = NotificationEventBuilder.buildBaseEvent(
            teamIds,
            null,
            "message",
            NotificationEvent.NotificationEventType.AUCTION_BID
        );

        assertNull(event, "Event should be null when teamIds are invalid");
    }
}