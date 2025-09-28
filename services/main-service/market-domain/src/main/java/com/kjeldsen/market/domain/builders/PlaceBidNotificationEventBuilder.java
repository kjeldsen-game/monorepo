package com.kjeldsen.market.domain.builders;

import com.kjeldsen.lib.builders.NotificationEventBuilder;
import com.kjeldsen.lib.events.NotificationEvent;

import java.util.List;
import java.util.Map;

public class PlaceBidNotificationEventBuilder extends NotificationEventBuilder {

    public static NotificationEvent build(List<String > teamIds, String playerId) throws IllegalArgumentException {

        if (playerId == null || playerId.isBlank()) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> payload = Map.of("playerId", playerId);

        return buildBaseEvent(
            teamIds,
            payload,
            "Another team has placed a bid on a player you are bidding for.",
            NotificationEvent.NotificationEventType.AUCTION_BID
        );
    }
}
