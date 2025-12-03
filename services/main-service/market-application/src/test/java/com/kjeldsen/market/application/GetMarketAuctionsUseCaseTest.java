//package com.kjeldsen.market.application;
//
//import com.kjeldsen.market.domain.Auction;
//import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
//import com.kjeldsen.player.domain.Player;
//import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.*;
//
//class GetMarketAuctionsUseCaseTest {
//
//    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
//    private final AuctionReadRepository mockedAuctionReadRepository = Mockito.mock(AuctionReadRepository.class);
//    private final GetMarketAuctionsUseCase getMarketAuctionsUseCase = new GetMarketAuctionsUseCase(
//        mockedPlayerReadRepository, mockedAuctionReadRepository);
//
//    @Test
//    @DisplayName("Should return auction player map")
//    void should_return_auction_player_map() {
//        Player mockedPlayer = Player.builder().id(Player.PlayerId.of("playerId")).build();
//        Auction mockedAuction = Auction.builder()
//            .status(Auction.AuctionStatus.ACTIVE).playerId("playerId")
//            .averageBid(BigDecimal.ONE).build();
//        when(mockedAuctionReadRepository.findAllByQuery(any())).thenReturn(List.of(mockedAuction));
//        when(mockedPlayerReadRepository.filterMarketPlayers(any())).thenReturn(List.of(mockedPlayer));
//
//        Map<Auction, Player> result = getMarketAuctionsUseCase.getAuctions(
//            10.0, 5.0, 40, 5, null, null, null, null);
//
//        assertEquals(1, result.size());
//        assertThat(result).containsKey(mockedAuction).containsValue(mockedPlayer);
//    }
//
//    @Test
//    @DisplayName("Should return empty auction player map")
//    void should_return_empty_auction_player_map() {
//        Auction mockedAuction = Auction.builder()
//            .status(Auction.AuctionStatus.ACTIVE).playerId("playerId")
//            .averageBid(BigDecimal.ONE).build();
//        when(mockedAuctionReadRepository.findAllByQuery(any())).thenReturn(List.of(mockedAuction));
//        when(mockedPlayerReadRepository.filterMarketPlayers(any())).thenReturn(Collections.emptyList());
//
//        Map<Auction, Player> result = getMarketAuctionsUseCase.getAuctions(
//            10.0, 5.0, 40, 5, null, null, null, null);
//
//        assertEquals(0, result.size());
//    }
//}