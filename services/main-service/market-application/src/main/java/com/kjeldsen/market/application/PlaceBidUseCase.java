package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceBidUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final AuctionReadRepository auctionReadRepository;
    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private static final Integer AUCTION_BID_REDUCE_TIME = 30;

    public void placeBid(Auction.AuctionId auctionId, BigDecimal amount, String userId) {
        log.info("PlaceBidUseCase for auction {}", auctionId);

        Auction auction = auctionReadRepository.findById(auctionId).orElseThrow(
            () -> new RuntimeException("Auction not found"));

        Team team = teamReadRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("Team not found"));

       if (team.getId().equals(auction.getTeamId())) {
           throw new RuntimeException("Auction creator team cannot place bid");
       }

       BigDecimal highestBidAmount = auction.getBids().stream()
            .filter(bid -> bid.getTeamId().equals(team.getId()))
            .map(Auction.Bid::getAmount)
            .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

       if (amount.compareTo(highestBidAmount) <= 0) {
            throw new RuntimeException("You cannot place less bid than your latest!");
        }
        BigDecimal bidAmountDiff = getDifferenceBetweenBids(amount, highestBidAmount);

       // Team don't have enough balance
       if (team.getEconomy().getBalance().compareTo(bidAmountDiff.abs()) < 0) {
           throw new RuntimeException("Bidder team don't have enough balance!");
       }

       // Subtract the money from the balance (so we have some record about money movement)
       team.getEconomy().updateBalance(bidAmountDiff);
        Auction.Bid bid = Auction.Bid.builder()
            .timestamp(InstantProvider.now())
            .teamId(team.getId())
            .amount(amount)
            .build();
        auction.reduceEndedAtBySeconds(AUCTION_BID_REDUCE_TIME);
        auction.getBids().add(bid);
        auction.setAverageBid(getAverageBid(auction.getBids()));

        auctionWriteRepository.save(auction);
        teamWriteRepository.save(team);
    }

    private BigDecimal getDifferenceBetweenBids(BigDecimal newBid, BigDecimal oldBid) {
        return oldBid.subtract(newBid);
    }

    private BigDecimal getAverageBid(List<Auction.Bid> bids) {
        BigDecimal a = bids.stream().map(Auction.Bid::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return a.divide(BigDecimal.valueOf(bids.size()), 1 , RoundingMode.HALF_UP);
    }
}
