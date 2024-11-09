package com.kjeldsen.market.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {

    @Test
    @DisplayName("Should create different auctionIds")
    void should_create_different_auctionIds() {
        Auction.AuctionId auctionId1 = Auction.AuctionId.generate();
        Auction.AuctionId auctionId2 = Auction.AuctionId.generate();
        assertNotEquals(auctionId1, auctionId2);
    }

    @Test
    @DisplayName("Should reduce endedAt time")
    void should_reduce_ended_at_time() {
        Auction mockedAuction = Auction.builder().endedAt(Instant.parse("2023-09-11T12:45:30Z")).build();
        mockedAuction.reduceEndedAtBySeconds(30);
        assertEquals("2023-09-11T12:45:00Z", mockedAuction.getEndedAt().toString());
    }

    @Test
    @DisplayName("Should assign right Id value with of")
    void should_assign_right_id_value_with_of() {
        String exampleId = "exampleId";
        Auction.AuctionId auctionId = Auction.AuctionId.of(exampleId);
        assertEquals(exampleId, auctionId.value());
    }
}