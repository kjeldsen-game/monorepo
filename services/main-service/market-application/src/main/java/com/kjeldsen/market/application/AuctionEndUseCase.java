package com.kjeldsen.market.application;

import com.kjeldsen.lib.events.market.AuctionEndEvent;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEndUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final AuctionReadRepository auctionReadRepository;

    public AuctionEndEvent endAuction(Auction.AuctionId auctionId) {
        log.info("AuctionEndUseCase for auction {}", auctionId );

        Auction auction = auctionReadRepository.findById(auctionId).orElseThrow(
            AuctionNotFoundException::new);

        Auction.Bid highestBid = auction.getBids().stream()
            .max(Comparator.comparing(Auction.Bid::getAmount)).orElseThrow(
            () -> new PlaceBidException("Auction bid not found!"));

        List<Auction.Bid> bids = new ArrayList<>();
        // Highest bid is from Team that put player on Market -> take him back to the Team
        if (!highestBid.getTeamId().equals(auction.getTeamId())) {
            bids = getHighestBidPerTeam(auction.getBids(), highestBid, auction.getTeamId());
        }

        auction.setStatus(Auction.AuctionStatus.COMPLETED);
        auctionWriteRepository.save(auction);

        return AuctionEndEvent.builder()
            .auctionWinner(highestBid.getTeamId())
            .auctionCreator(auction.getTeamId())
            .amount(highestBid.getAmount())
            .playerId(auction.getPlayerId())
            .bidders(
                bids.stream()
                    .collect(Collectors.toMap(
                        Auction.Bid::getTeamId,
                        Auction.Bid::getAmount
                    )))
            .build();
    }

    private List<Auction.Bid> getHighestBidPerTeam(List<Auction.Bid> bids, Auction.Bid highestBid, String creatorTeamId) {
        return bids.stream()
            .collect(Collectors.groupingBy(
                Auction.Bid::getTeamId,
                Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparing(Auction.Bid::getAmount)),
                    optionalBid -> optionalBid.orElse(null)
                )
            ))
            .values().stream()
            .filter(Objects::nonNull)
            .filter(bid -> {
                var teamId = bid.getTeamId();
                return !teamId.equals(highestBid.getTeamId()) &&
                    !teamId.equals(creatorTeamId);
            })
            .toList();
    }
}
