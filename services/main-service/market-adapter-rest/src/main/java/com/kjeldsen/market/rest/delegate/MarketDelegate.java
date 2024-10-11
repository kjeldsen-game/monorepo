package com.kjeldsen.market.rest.delegate;

import com.kjeldsen.auth.AuthService;
import com.kjeldsen.market.application.AuctionEndUseCase;
import com.kjeldsen.market.application.GetMarketAuctionsUseCase;
import com.kjeldsen.market.application.PlaceBidUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.publishers.AuctionEndEventPublisher;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import com.kjeldsen.market.rest.api.MarketApiDelegate;
import com.kjeldsen.market.rest.mapper.AuctionMapper;
import com.kjeldsen.market.rest.mapper.PlayerMapper;
import com.kjeldsen.market.rest.model.*;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDelegate implements MarketApiDelegate {
    private final PlaceBidUseCase placeBidUseCase;
    private final AuctionEndUseCase auctionEndUseCase;
    private final AuctionEndEventPublisher auctionEndEventPublisher;
    private final AuthService authService;
    private final AuctionReadRepository auctionReadRepository;
    private final GetMarketAuctionsUseCase getMarketAuctionsUseCase;
    private final AuctionEndJobScheduler auctionEndJobScheduler;

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
        Auction auction = placeBidUseCase.placeBid(Auction.AuctionId.of(auctionId), amount, currentUseId.get());
        auctionEndJobScheduler.rescheduleAuctionEndJob(auctionId, auction.getEndedAt());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateAuctionEnd(String auctionId) {
        AuctionEndEvent auctionEndEvent = auctionEndUseCase.endAuction(Auction.AuctionId.of(auctionId));
        auctionEndEventPublisher.publishAuctionEndEvent(auctionEndEvent);
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<List<MarketAuctionResponse>> getAllAuctions(Integer size, Integer page,
        PlayerPosition position, String skills, Integer minAge, Integer maxAge, Double minBid, Double maxBid) {

        Map<Auction, Player> auctionPlayerMap = getMarketAuctionsUseCase.getAuctions(maxBid, minBid, maxAge, minAge,
            PlayerMapper.INSTANCE.playerPositionMap(position), skills);

        List<MarketAuctionResponse> marketAuctionResponses = auctionPlayerMap.entrySet().stream()
            .map(entry -> {
                Auction auction = entry.getKey();
                Player player = entry.getValue();
                MarketPlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);

                return new MarketAuctionResponse().auctionId(
                    auction.getId().value()).averageBid(auction.getAverageBid())
                    .averageBid(auction.getAverageBid())
                    .player(playerResponse);
            })
            .toList();

        return ResponseEntity.ok(marketAuctionResponses);
    }

}
