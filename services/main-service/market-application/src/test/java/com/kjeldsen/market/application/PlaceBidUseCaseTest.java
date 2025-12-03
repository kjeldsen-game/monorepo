package com.kjeldsen.market.application;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.EconomyClient;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.InsufficientBalanceException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.provider.InstantProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceBidUseCaseTest {

    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
    private final AuctionWriteRepository mockedAuctionWriteRepository = Mockito.mock(AuctionWriteRepository.class);
    private final GenericEventPublisher mockedBidEventPublisher = Mockito.mock(GenericEventPublisher.class);
    private final TeamClientApi mockedTeamClientApi = Mockito.mock(TeamClientApi.class);
    private final PlaceBidUseCase placeBidUseCase = new PlaceBidUseCase(mockedAuctionWriteRepository, mockedAuctionReadRepository,
        mockedBidEventPublisher, mockedTeamClientApi);

    private static String mockedTeamId;

    @BeforeAll
    static void beforeAll() {
        mockedTeamId = "mockedTeamId";
    }

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
    @DisplayName("Should throw exception if response from TeamClient is empty")
    void should_throw_exception_if_team_is_null() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Auction mockedAuction = Mockito.mock(Auction.class);
        String mockedUserId = UUID.randomUUID().toString();
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamClientApi.getTeam(null, null, mockedUserId)).thenReturn(Collections.emptyList());

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
        TeamClient mockedTeam = TeamClient.builder().id("teamId").build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedAuction.getTeamId()).thenReturn("teamId");
        when(mockedTeamClientApi.getTeam(null, null, mockedUserId)).thenReturn(List.of(mockedTeam));

        assertEquals("Cannot place new bid on auction you created!", assertThrows(PlaceBidException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ONE, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if bid had smaller amount that was already placed by team")
    void should_throw_exception_if_bid_had_smaller_amount() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        TeamClient mockedTeam = Mockito.mock(TeamClient.class);
        String teamId = "teamId";

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                    Auction.Bid.builder().teamId(teamId).amount(BigDecimal.TEN).build()
            ))).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamClientApi.getTeam(null, null, mockedUserId)).thenReturn(List.of(mockedTeam));
        when(mockedTeam.getId()).thenReturn(teamId);

        assertEquals("You cannot place less bid than your latest!", assertThrows(PlaceBidException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.ZERO, mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if team dont have enough amount in balance")
    void should_throw_exception_if_team_dont_have_enough_balance() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        TeamClient mockedTeam = TeamClient.builder().id(mockedTeamId).economy(
            EconomyClient.builder().balance(BigDecimal.ZERO).build()).build();

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                Auction.Bid.builder().teamId(mockedTeamId).amount(BigDecimal.TEN).build()
            ))).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamClientApi.getTeam(null, null, mockedUserId)).thenReturn(List.of(mockedTeam));

        assertEquals("You don't have enough balance to place bid!", assertThrows(InsufficientBalanceException.class, () -> {
            placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.valueOf(11), mockedUserId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should place a bid and save the auction")
    void should_place_bid() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        String mockedUserId = UUID.randomUUID().toString();
        TeamClient mockedTeam = TeamClient.builder().id(mockedTeamId).economy(
                EconomyClient.builder().balance(BigDecimal.valueOf(1000)).build()).build();

        MockedStatic<InstantProvider> mockedStatic = mockStatic(InstantProvider.class);
        mockedStatic.when(InstantProvider::now).thenReturn(Instant.parse("2024-09-10T10:00:00Z"));

        Auction mockedAuction = Auction.builder()
            .bids(new ArrayList<>(List.of(
                    Auction.Bid.builder().teamId(mockedTeamId).amount(BigDecimal.ONE).build()
            ))).averageBid(BigDecimal.ONE).endedAt(InstantProvider.now()).build();

        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));
        when(mockedTeamClientApi.getTeam(null, null, mockedUserId)).thenReturn(List.of(mockedTeam));


        placeBidUseCase.placeBid(mockedAuctionId, BigDecimal.TEN, mockedUserId);

        assertEquals(1, mockedAuction.getBids().size());
        assertEquals(BigDecimal.TEN, mockedAuction.getBids().get(mockedAuction.getBids().size()-1).getAmount());
        assertEquals(BigDecimal.valueOf(10.0), mockedAuction.getAverageBid());
        assertEquals("2024-09-10T09:59:30Z", mockedAuction.getEndedAt().toString());
        verify(mockedAuctionWriteRepository).save(mockedAuction);
    }
}