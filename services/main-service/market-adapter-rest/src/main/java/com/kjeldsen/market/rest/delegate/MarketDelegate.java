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
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
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
    private final TeamReadRepository teamReadRepository;
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
        //auctionEndJobScheduler.rescheduleAuctionEndJob(auctionId, auction.getEndedAt());
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
        PlayerPosition position, String skills, String potentialSkills,  Integer minAge, Integer maxAge, Double minBid, Double maxBid, String playerId) {
        log.info("Get all auctions");
        Optional<String> currentUserId = authService.currentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Team team = teamReadRepository.findByUserId(currentUserId.get()).orElseThrow(
            () -> new RuntimeException("Team not found"));

        Map<Auction, Player> auctionPlayerMap = getMarketAuctionsUseCase.getAuctions(maxBid, minBid, maxAge, minAge,
            position != null ? PlayerMapper.INSTANCE.playerPositionMap(position) : null, skills, potentialSkills,
            playerId);

        List<MarketAuctionResponse> marketAuctionResponses = auctionPlayerMap.entrySet().stream()
            .map(entry -> {
                Auction auction = entry.getKey();
                Player player = entry.getValue();
                MarketPlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);

                Auction.Bid highestBid = auction.getBids().stream()
                    .filter(bid -> bid.getTeamId().equals(team.getId()))
                    .max(Comparator.comparing(Auction.Bid::getAmount))
                    .orElse(null);

                return new MarketAuctionResponse().id(
                    auction.getId().value())
                    .averageBid(auction.getAverageBid())
                    .bidders(auction.getBids().size())
                    .teamId(auction.getTeamId().value())
                    .bid(highestBid != null ? highestBid.getAmount() : null)
                    .endedAt(auction.getEndedAt().toString())
                    .player(playerResponse);
            })
            .toList();

        return ResponseEntity.ok(marketAuctionResponses);
    }

}
