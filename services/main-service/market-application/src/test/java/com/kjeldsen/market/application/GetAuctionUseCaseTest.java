package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GetAuctionUseCaseTest {

    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
    private final GetAuctionUseCase getAuctionUseCase = new GetAuctionUseCase(mockedAuctionReadRepository);

    @Test
    @DisplayName("Should return auction player map")
    void should_return_auction_player_map() {
        when(mockedAuctionReadRepository.findAllByQuery(any())).thenReturn(List.of());
        List<Auction> result = getAuctionUseCase.getAuctions(
            10.0, 5.0, 40, 5, null, null, null, null);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return auctions when found")
    void should_return_auctions_when_found() {
        List<Auction> auctions = List.of(
            Auction.builder()
                .id(Auction.AuctionId.of("1"))
                .averageBid(BigDecimal.valueOf(7.5))
                .build(),
            Auction.builder()
                .id(Auction.AuctionId.of("2"))
                .averageBid(BigDecimal.valueOf(9.0))
                .build()
        );

        when(mockedAuctionReadRepository.findAllByQuery(any())).thenReturn(auctions);
        List<Auction> result = getAuctionUseCase.getAuctions(
            10.0, 5.0, 40, 5, null, null, null, null);
        assertEquals(2, result.size());

    }
}