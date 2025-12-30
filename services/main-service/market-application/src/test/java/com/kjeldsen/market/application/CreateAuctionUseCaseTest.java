package com.kjeldsen.market.application;

import com.kjeldsen.lib.clients.PlayerClientApi;
import com.kjeldsen.lib.model.player.PlayerClient;
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
    private final PlayerClientApi mockedPlayerClientApi = Mockito.mock(PlayerClientApi.class);
    private final CreateAuctionUseCase createAuctionUseCase = new CreateAuctionUseCase(auctionWriteRepository, mockedPlayerClientApi);

//    @Test
//    @DisplayName("Should create a Auction and save it")
//    void should_create_auction_and_save_it() {
//        String mockedPlayerId = "playerId";
//        String mockedTeamId = "teamId";
//        PlayerClient playerClient = PlayerClient.builder()
//                .id(mockedPlayerId)
//                .name("Player Name")
//                .build();
//        when(mockedPlayerClientApi.getPlayer(mockedPlayerId)).thenReturn(playerClient);
//
//        createAuctionUseCase.create(mockedPlayerId, mockedTeamId);
//
//        verify(auctionWriteRepository).save(any(Auction.class));
//
//        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
//        verify(auctionWriteRepository).save(auctionCaptor.capture());
//        Auction savedAuction = auctionCaptor.getValue();
//
//        assertNotNull(savedAuction);
//        assertEquals(mockedTeamId.value(), savedAuction.getTeamId());
//        assertEquals(mockedPlayerId.value(), savedAuction.getPlayerId());
//    }
}