package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAuctionUseCaseTest {

    private final AuctionWriteRepository auctionWriteRepository = Mockito.mock(AuctionWriteRepository.class);
    private final CreateAuctionUseCase createAuctionUseCase = new CreateAuctionUseCase(auctionWriteRepository);

    @Test
    @DisplayName("Should create a Auction and save it")
    public void should_create_auction_and_save_it() {
        Player.PlayerId mockedPlayerId = Player.PlayerId.generate();
        Team.TeamId mockedTeamId = Team.TeamId.generate();

        createAuctionUseCase.create(mockedPlayerId, mockedTeamId);

        verify(auctionWriteRepository).save(any(Auction.class));

        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionWriteRepository).save(auctionCaptor.capture());
        Auction savedAuction = auctionCaptor.getValue();

        assertNotNull(savedAuction);
        assertEquals(mockedTeamId, savedAuction.getTeamId());
        assertEquals(mockedPlayerId, savedAuction.getPlayerId());
    }
}