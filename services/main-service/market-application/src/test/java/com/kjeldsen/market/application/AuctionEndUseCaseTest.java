package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionEndUseCaseTest {

    private final AuctionWriteRepository mockedAuctionWriteRepository = Mockito.mock(AuctionWriteRepository.class);
    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final AuctionEndUseCase auctionEndUseCase = new AuctionEndUseCase(mockedAuctionWriteRepository,
        mockedAuctionReadRepository, mockedTeamReadRepository, mockedTeamWriteRepository);

    @Test
    @DisplayName("Should throw exception if auction is null")
    public void should_throw_exception_if_auction_is_null() {
        Auction.AuctionId mockedAuctionId = Mockito.mock(Auction.AuctionId.class);
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.empty());

        assertEquals("Auction not found", assertThrows(RuntimeException.class, () -> {
            auctionEndUseCase.endAuction(mockedAuctionId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if there are no bids")
    public void should_throw_exception_if_there_are_no_bids() {
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Auction mockedAuction = Mockito.mock(Auction.class);
        when(mockedAuction.getBids()).thenReturn(List.of());
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));

        assertEquals("Auction bid not found", assertThrows(RuntimeException.class, () -> {
            auctionEndUseCase.endAuction(mockedAuctionId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should return highest bid amount to every team and update auction")
    public void should_return_highest_bid_amount_to_every_team_and_update_auction() {
        Team.TeamId creatorTeamId = Team.TeamId.generate();
        Team.TeamId winnerTeamId = Team.TeamId.generate();
        Team creatorTeam = Team.builder().id(creatorTeamId).economy(
            Team.Economy.builder().balance(BigDecimal.valueOf(1000)).build()).build();
        Team winnerTeam = Team.builder().id(winnerTeamId).economy(
            Team.Economy.builder().balance(BigDecimal.valueOf(1000)).build()).build();
        Auction.AuctionId mockedAuctionId = Auction.AuctionId.generate();
        Player.PlayerId mockedPlayerId = Player.PlayerId.generate();
        Auction mockedAuction = Auction.builder()
            .id(mockedAuctionId)
            .teamId(creatorTeamId)
            .status(Auction.AuctionStatus.ACTIVE)
            .playerId(mockedPlayerId)
            .bids(List.of(
                    Auction.Bid.builder().teamId(creatorTeamId).amount(BigDecimal.TEN).build(),
                    Auction.Bid.builder().teamId(winnerTeamId).amount(BigDecimal.valueOf(11)).build(),
                    Auction.Bid.builder().teamId(winnerTeamId).amount(BigDecimal.valueOf(100)).build()
            ))
            .build();

        for (Team team : new Team[]{creatorTeam, winnerTeam}) {
            when(mockedTeamReadRepository.findById(team.getId())).thenReturn(Optional.of(team));
        }
        when(mockedAuctionReadRepository.findById(mockedAuctionId)).thenReturn(Optional.of(mockedAuction));

        AuctionEndEvent testEvent = auctionEndUseCase.endAuction(mockedAuctionId);
        assertEquals(Auction.AuctionStatus.COMPLETED, mockedAuction.getStatus());
        assertEquals(BigDecimal.valueOf(100), testEvent.getAmount());
        assertEquals(creatorTeamId, testEvent.getAuctionCreator());
        assertEquals(winnerTeamId, testEvent.getAuctionWinner());
        assertEquals(BigDecimal.valueOf(1010), creatorTeam.getEconomy().getBalance());
        assertEquals(BigDecimal.valueOf(1100), winnerTeam.getEconomy().getBalance());
        verify(mockedAuctionReadRepository).findById(mockedAuctionId);
        verify(mockedAuctionWriteRepository).save(mockedAuction);
    }
}