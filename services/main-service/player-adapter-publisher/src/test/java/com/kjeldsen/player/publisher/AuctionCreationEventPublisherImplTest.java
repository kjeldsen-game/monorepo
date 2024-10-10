package com.kjeldsen.player.publisher;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import com.kjeldsen.player.domain.publishers.AuctionCreationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@Slf4j
@RecordApplicationEvents
class AuctionCreationEventPublisherImplTest extends TestEventListener<AuctionCreationEvent> {

    @Autowired
    private AuctionCreationEventPublisher publisher;

    @Autowired
    private ApplicationEvents events;

    @Test
    void test() {
        publisher.publishAuctionCreationEvent(AuctionCreationEvent.builder().build());
        assertEquals(String.valueOf(1),String.valueOf(events.stream(AuctionCreationEvent.class).count()));
    }
}