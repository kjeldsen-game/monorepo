package com.kjeldsen.market.rest.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.auth.AuthService;
import com.kjeldsen.market.application.AuctionEndUseCase;
import com.kjeldsen.market.application.PlaceBidUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.publishers.AuctionEndEventPublisher;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.rest.api.MarketApiDelegate;
import com.kjeldsen.market.rest.mapper.AuctionMapper;
import com.kjeldsen.market.rest.mapper.AuctionPlayerMapper;
import com.kjeldsen.market.rest.mapper.PlayerMapper;
import com.kjeldsen.market.rest.model.*;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDelegate implements MarketApiDelegate {
    private final PlaceBidUseCase placeBidUseCase;
    private final AuctionEndUseCase auctionEndUseCase;
    private final AuctionEndEventPublisher auctionEndEventPublisher;
    private final AuthService authService;
    private final AuctionReadRepository auctionReadRepository;
    private final PlayerReadRepository playerReadRepository;

    @Override
    public ResponseEntity<AuctionResponse> getAuctionById(String auctionId) {
        Auction auction = auctionReadRepository.findById(Auction.AuctionId.of(auctionId)).orElseThrow();
        AuctionResponse response = AuctionMapper.INSTANCE.auctionResponseMap(auction);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> placeAuctionBid(String auctionId, PlaceAuctionBidRequest placeAuctionBidRequest) {
        Optional<String> currentUseId = authService.currentUserId();
        if (currentUseId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        BigDecimal amount = BigDecimal.valueOf(placeAuctionBidRequest.getAmount());
        placeBidUseCase.placeBid(Auction.AuctionId.of(auctionId), amount, currentUseId.get());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateAuctionEnd(String auctionId) {
        AuctionEndEvent auctionEndEvent = auctionEndUseCase.endAuction(Auction.AuctionId.of(auctionId));
        auctionEndEventPublisher.publishAuctionEndEvent(auctionEndEvent);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<MarketAuctionResponse>> getAllAuctions(Integer page, Integer size) {
        List<Auction> auctions = auctionReadRepository.findAll(); //Add here pagination -> then get plaeyrs and map
        List<Player.PlayerId> playerIds = auctions.stream().map(Auction::getPlayerId).toList();
        List<String> playerIdValues = playerIds.stream()
                .map(Player.PlayerId::value) // Extract value from Player.PlayerId
                .toList();

        List<Player> players = playerReadRepository.findByPlayersIds(playerIdValues);

        AuctionPlayerMapper apm = new AuctionPlayerMapper();
        Map<Auction, Player> aa = apm.mapAuctionsToPlayers(auctions, players);

        for (Map.Entry<Auction, Player> entry : aa.entrySet()) {

            Auction auction = entry.getKey();    // The Auction object
            Player player = entry.getValue();    // The corresponding Player object

            // You can print or process the Auction and Player objects
            System.out.println("Auction ID: " + auction.getId());
            System.out.println("Player ID: " + player.getId());
        }

        List<MarketAuctionResponse> marketAuctionResponses = aa.entrySet().stream()
                .map(entry -> {
                    Auction auction = entry.getKey();    // The Auction object
                    Player player = entry.getValue();    // The corresponding Player object

                    // Create PlayerResponse object from the Player
                    PlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);

                    // Create and return MarketAuctionResponse
                    return new MarketAuctionResponse().auctionId(
                        auction.getId().value()).averageBid(auction.getAverageBid())
                        .averageBid(auction.getAverageBid())
                        .player(playerResponse);
                })
                .toList();

        return ResponseEntity.ok(marketAuctionResponses);
    }
}
