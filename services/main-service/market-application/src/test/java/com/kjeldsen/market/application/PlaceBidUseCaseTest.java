package com.kjeldsen.market.application;

import com.kjeldsen.lib.events.BidEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.InsufficientBalanceException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
class PlaceBidUseCaseTest {

    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
    private final AuctionWriteRepository mockedAuctionWriteRepository = Mockito.mock(AuctionWriteRepository.class);
    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final GenericEventPublisher mockedBidEventPublisher = Mockito.mock(GenericEventPublisher.class);
    private final PlaceBidUseCase placeBidUseCase = new PlaceBidUseCase(mockedAuctionWriteRepository, mockedAuctionReadRepository
    , mockedTeamReadRepository, mockedTeamWriteRepository, mockedBidEventPublisher);

    @Test
    @DisplayName("Should throw exception if Auction is null")
    void should_throw_exception_if_auction_is_null() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.empty());

        assertEquals("Auction not found!", assertThrows(AuctionNotFoundException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ONE, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if Team is null")
    void should_throw_exception_if_team_is_null() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Auction mockedAuction = Mockito.mock(Auction.class);
        String mockedUserId = UUID.randomUUID().toString();
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamReadRepository.findByUserId(mockedUserId)).thenReturn(Optional.empty());

        assertEquals("Team not found!", assertThrows(TeamNotFoundException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ONE, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if bidding team is auction creator team")
    void should_throw_exception_if_bidding_team_is_auction_creator_team() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Auction mockedAuction = Mockito.mock(Auction.class);
        String mockedUserId = UUID.randomUUID().toString();
        Team.TeamId mockedTeamId = Team.TeamId.generate();
        Team mockedTeam = Team.builder().id(mockedTeamId).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedAuction.getTeamId()).thenReturn(mockedTeamId.value());
        when(mockedTeamReadRepository.findByUserId(mockedUserId)).thenReturn(Optional.of(mockedTeam));

        assertEquals("Auction creator team cannot place bid!", assertThrows(PlaceBidException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ONE, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if bid had smaller amount that was already placed by team")
    void should_throw_exception_if_bid_had_smaller_amount() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        Team mockedTeam = Mockito.mock(Team.class);
        Team.TeamId mockedTeamId = Team.TeamId.generate();

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                    Auction.Bid.builder().teamId(mockedTeamId.value()).amount(BigDecimal.TEN).build()
            ))).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamReadRepository.findByUserId(mockedUserId)).thenReturn(Optional.of(mockedTeam));
        when(mockedTeam.getId()).thenReturn(mockedTeamId);

        assertEquals("You cannot place less bid than your latest!", assertThrows(PlaceBidException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ZERO, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if team dont have enough amount in balance")
    void should_throw_exception_if_team_dont_have_enough_balance() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        Team.TeamId mockedTeamId = Team.TeamId.generate();
        Team mockedTeam = Team.builder().id(mockedTeamId).economy(
            Team.Economy.builder().balance(BigDecimal.ZERO).build()).build();

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                Auction.Bid.builder().teamId(mockedTeamId.value()).amount(BigDecimal.TEN).build()
            ))).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamReadRepository.findByUserId(mockedUserId)).thenReturn(Optional.of(mockedTeam));

        assertEquals("You don't have enough balance to place bid!", assertThrows(InsufficientBalanceException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.valueOf(11), mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should place a bid and save the auction")
    void should_place_bid() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        Team.TeamId mockedTeamId = Team.TeamId.generate();
        Team mockedTeam = Team.builder().id(mockedTeamId).economy(
                Team.Economy.builder().balance(BigDecimal.valueOf(1000)).build()).build();

        MockedStatic<InstantProvider> mockedStatic = mockStatic(InstantProvider.class);
        mockedStatic.when(InstantProvider::now).thenReturn(Instant.parse("2024-09-10T10:00:00Z"));

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                    Auction.Bid.builder().teamId(mockedTeamId.value()).amount(BigDecimal.ONE).build()
            ))).averageBid(BigDecimal.ONE).endedAt(InstantProvider.now()).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamReadRepository.findByUserId(mockedUserId)).thenReturn(Optional.of(mockedTeam));


        placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.TEN, mockedUserId);

        assertEquals(1, mockedAuction.getBids().size());
        assertEquals(BigDecimal.TEN, mockedAuction.getBids().get(mockedAuction.getBids().size()-1).getAmount());
        assertEquals(BigDecimal.valueOf(991), mockedTeam.getEconomy().getBalance());
        assertEquals(BigDecimal.valueOf(10.0), mockedAuction.getAverageBid());
        assertEquals("2024-09-10T09:59:30Z", mockedAuction.getEndedAt().toString());
        verify(mockedAuctionWriteRepository).save(mockedAuction);
        verify(mockedTeamWriteRepository).save(mockedTeam);
    }
}