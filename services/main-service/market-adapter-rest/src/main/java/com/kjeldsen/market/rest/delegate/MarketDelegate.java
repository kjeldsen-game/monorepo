package com.kjeldsen.market.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.events.market.AuctionEndEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.market.application.AuctionEndUseCase;
import com.kjeldsen.market.application.GetAuctionUseCase;
import com.kjeldsen.market.application.PlaceBidUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.rest.api.MarketApiDelegate;
import com.kjeldsen.market.rest.mapper.AuctionMapper;
import com.kjeldsen.market.rest.mapper.PlayerMapper;
import com.kjeldsen.market.rest.model.*;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDelegate implements MarketApiDelegate {
    private final PlaceBidUseCase placeBidUseCase;
    private final AuctionEndUseCase auctionEndUseCase;
    private final GenericEventPublisher auctionEndEventPublisher;
    private final AuctionReadRepository auctionReadRepository;
    private final GetAuctionUseCase getAuctionUseCase;
    private final TeamClientApi teamClientApi;

//    @Override
//    public ResponseEntity<AuctionResponse> getAuctionById(String auctionId) {
//        Auction auction = auctionReadRepository.findById(Auction.AuctionId.of(auctionId)).orElseThrow();
////        AuctionResponse response = AuctionMapper.INSTANCE.auctionResponseMap(auction);
//        return ResponseEntity.ok(null);
//    }

    @Override
    public ResponseEntity<SuccessResponse> placeAuctionBid(String auctionId, PlaceAuctionBidRequest placeAuctionBidRequest) {
        String currentUseId = SecurityUtils.getCurrentUserId();
        BigDecimal amount = BigDecimal.valueOf(placeAuctionBidRequest.getAmount());
        Auction auction = placeBidUseCase.placeBid(Auction.AuctionId.of(auctionId), amount, currentUseId);
        //auctionEndJobScheduler.rescheduleAuctionEndJob(auctionId, auction.getEndedAt());
        return ResponseEntity.ok(new SuccessResponse()
            .message(String.format("Successfully placed auction bid of %s $!", amount)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> simulateAuctionEnd(String auctionId) {
        AuctionEndEvent auctionEndEvent = auctionEndUseCase.endAuction(Auction.AuctionId.of(auctionId));
        auctionEndEventPublisher.publishEvent(auctionEndEvent);
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<List<AuctionResponse>> getAllAuctions(Integer size, Integer page,
        PlayerPosition position, String skills, String potentialSkills, Integer minAge, Integer maxAge, Double minBid, Double maxBid,
        String playerId) {

        TeamResponse team = teamClientApi.getTeams( null, SecurityUtils.getCurrentUserId()).get(0);

        List<Auction> auctions = getAuctionUseCase.getAuctions(maxBid, minBid, maxAge, minAge,
            position != null ? PlayerMapper.INSTANCE.playerPositionMap(position) : null, skills, potentialSkills,
            playerId);

        List<AuctionResponse> response = auctions.stream()
            .map(auction -> {
                AuctionResponse dto = AuctionMapper.INSTANCE.map(auction);
                dto.setBids(null);
                dto.setBid(
                    auction.getBids().stream()
                        .filter(bid -> bid.getTeamId().equals(team.getId()))
                        .map(Auction.Bid::getAmount)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO));
                return dto;
            }).toList();

        return ResponseEntity.ok(response);
    }
}
