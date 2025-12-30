package com.kjeldsen.market.application;

import com.kjeldsen.lib.events.market.AuctionEndEvent;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.AuctionPlayer;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionEndUseCaseTest {

    private final AuctionWriteRepository mockedAuctionWriteRepository = Mockito.mock(AuctionWriteRepository.class);
    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
    private final AuctionEndUseCase auctionEndUseCase = new AuctionEndUseCase(mockedAuctionWriteRepository,
        mockedAuctionReadRepository);

    @Test
    @DisplayName("Should throw exception if auction is null")
    void should_throw_exception_if_auction_is_null() {
        Auction.AuctionId mockedAuctionId = Mockito.mock(Auction.AuctionId.class);
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.empty());

        assertEquals("Auction not found!", assertThrows(AuctionNotFoundException.class, () -> {
            auctionEndUseCase.endAuction(mockedAuctionId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should set CANCELED status if there are no bids")
    void should_throw_exception_if_there_are_no_bids() {
        AuctionPlayer mockedPlayer = Mockito.mock(AuctionPlayer.class);
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Auction mockedAuction = Auction.builder().player(mockedPlayer).bids(Collections.emptyList()).build();
        when(mockedPlayer.getId()).thenReturn("123");
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        auctionEndUseCase.endAuction(mockedAuctionId);
        assertEquals(Auction.AuctionStatus.CANCELED, mockedAuction.getStatus());
    }

    @Test
    @DisplayName("Should return highest bid amount to every team and update auction")
    void should_return_highest_bid_amount_to_every_team_and_update_auction() {
        String creatorTeamId = "creatorTeamId";
        String winnerTeamId = "winnerTeamId";
        String bidderTeamId = "bidderTeamId";

        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();

        Auction mockedAuction = Auction.builder()
            .id(mockedAuctionId)
            .teamId(creatorTeamId)
            .status(Auction.AuctionStatus.ACTIVE)
            .player(AuctionPlayer.builder().id("1234").build())
            .bids(List.of(
                Auction.Bid.builder().teamId(creatorTeamId).amount(BigDecimal.TEN).build(),
                Auction.Bid.builder().teamId(winnerTeamId).amount(BigDecimal.valueOf(11)).build(),
                Auction.Bid.builder().teamId(bidderTeamId).amount(BigDecimal.valueOf(12)).build(),
                Auction.Bid.builder().teamId(winnerTeamId).amount(BigDecimal.valueOf(100)).build()
            ))
            .build();


        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));

        AuctionEndEvent testEvent = auctionEndUseCase.endAuction(mockedAuctionId);
        assertEquals(Auction.AuctionStatus.COMPLETED, mockedAuction.getStatus());
        assertEquals(BigDecimal.valueOf(100), testEvent.getAmount());
        assertEquals(creatorTeamId, testEvent.getAuctionCreator());
        assertEquals(winnerTeamId, testEvent.getAuctionWinner());
        assertEquals(Map.of(bidderTeamId, BigDecimal.valueOf(12)), testEvent.getBidders());

        verify(mockedAuctionReadRepository).findById(mockedAuctionId);
        verify(mockedAuctionWriteRepository).save(mockedAuction);
    }
}