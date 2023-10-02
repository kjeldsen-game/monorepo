package com.kjeldsen.match.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MatchCreatedEventTest {

    @Test
    void testSuperClassFieldsSet() {
        MatchCreatedEvent matchCreatedEvent = MatchCreatedEvent.builder().build();

        assertNotNull(matchCreatedEvent.eventId);
        assertNotNull(matchCreatedEvent.eventDate);
    }
}
