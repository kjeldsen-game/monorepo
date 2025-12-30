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
            .max(Comparator.comparing(Auction.Bid::getAmount)).orElse(null);

        log.info("AuctionEndUseCase for auction {}", highestBid );
        List<Auction.Bid> bids = new ArrayList<>();
        if (highestBid == null) {
            // CANCELED auction - no bids placed
            auction.setStatus(Auction.AuctionStatus.CANCELED);
        } else {
            auction.setStatus(Auction.AuctionStatus.COMPLETED);
            bids = getHighestBidPerTeam(auction.getBids(), highestBid, auction.getTeamId());
            auction.setWinnerTeamId(auction.getWinnerTeamId());
        }

        auctionWriteRepository.save(auction);

        return AuctionEndEvent.builder()
            .auctionWinner(highestBid == null ? null : highestBid.getTeamId())
            .auctionCreator(auction.getTeamId())
            .amount(highestBid== null ? null : highestBid.getAmount())
            .playerId(auction.getPlayer().getId())
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
