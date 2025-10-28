package com.kjeldsen.match.domain.builders;

import com.kjeldsen.lib.builders.NotificationEventBuilder;
import com.kjeldsen.lib.events.NotificationEvent;

import java.util.List;

public class LeagueStartEventBuilder extends NotificationEventBuilder {

    public static NotificationEvent build(List<String> teamIds) {

        return buildBaseEvent(
            teamIds,
            null,
            "League has started. Check League tab for Standings and Calendar.",
            NotificationEvent.NotificationEventType.LEAGUE_START
        );
    }
}
