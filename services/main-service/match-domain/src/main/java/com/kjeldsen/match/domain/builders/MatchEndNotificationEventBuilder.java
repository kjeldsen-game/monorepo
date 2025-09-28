package com.kjeldsen.match.domain.builders;

import com.kjeldsen.lib.builders.NotificationEventBuilder;
import com.kjeldsen.lib.events.NotificationEvent;

import java.util.List;
import java.util.Map;

public class MatchEndNotificationEventBuilder extends NotificationEventBuilder {

    public static NotificationEvent build(List<String> teamIds, String matchId) {

        if (matchId == null) {
            throw new IllegalArgumentException();
        }

        return buildBaseEvent(
            teamIds,
            Map.of("matchId", matchId),
            "Your match has ended.",
            NotificationEvent.NotificationEventType.MATCH_END
        );
    }
}
