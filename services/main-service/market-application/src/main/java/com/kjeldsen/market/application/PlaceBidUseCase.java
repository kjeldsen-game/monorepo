package com.kjeldsen.market.application;

import com.kjeldsen.lib.events.BidEvent;
import com.kjeldsen.lib.events.NotificationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.builders.PlaceBidNotificationEventBuilder;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.InsufficientBalanceException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.exceptions.TeamNotFoundException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// TODO REFACTOR

@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceBidUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final AuctionReadRepository auctionReadRepository;
    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final GenericEventPublisher eventPublisher;


    private static final Integer AUCTION_BID_REDUCE_TIME = 30;

    public Auction placeBid(Auction.AuctionId auctionId, BigDecimal amount, String userId) {
        log.info("PlaceBidUseCase for auction={} of amount={} from userId={}", auctionId, amount, userId);

        Auction auction = auctionReadRepository.findById(auctionId).orElseThrow(
            AuctionNotFoundException::new);

        Team team = teamReadRepository.findByUserId(userId).orElseThrow(
            TeamNotFoundException::new);

        if (Objects.equals(auction.getTeamId(), team.getId().value())) {
            throw new PlaceBidException("Cannot place new bid on auction you created!");
        }

        Auction.Bid highestBid = auction.getBids().stream()
            .filter(bid -> bid.getTeamId().equals(team.getId().value()))
            .max(Comparator.comparing(Auction.Bid::getAmount))
            .orElse(null);

        BigDecimal bidAmountDiff = getDifferenceBetweenBids(amount, highestBid == null ?
            BigDecimal.ZERO : highestBid.getAmount());

        if (highestBid != null) {
           validateBidAmount(amount, highestBid.getAmount());
           updateTeamBalance(team, bidAmountDiff);
           highestBid.setAmount(highestBid.getAmount().add(bidAmountDiff.abs()));
       } else {
           updateTeamBalance(team, bidAmountDiff);
           Auction.Bid bid = Auction.Bid.builder()
           .timestamp(InstantProvider.now())
           .teamId(team.getId().value())
           .amount(amount)
           .build();
           auction.getBids().add(bid);
       }

        auction.reduceEndedAtBySeconds(AUCTION_BID_REDUCE_TIME);
        auction.setAverageBid(getAverageBid(auction.getBids()));

        List<String> teamIds = auction.getBids().stream()
            .map(Auction.Bid::getTeamId)
            .filter(bidTeamId -> {
                String auctionOwnerId = auction.getTeamId();
                String currentBidderId = team.getId().value();
                return !bidTeamId.equals(auctionOwnerId) && !bidTeamId.equals(currentBidderId);
            })
            .distinct()
            .toList();

        eventPublisher.publishEvent(
            PlaceBidNotificationEventBuilder.build(teamIds, auction.getPlayerId())
        );

        teamWriteRepository.save(team);
        return auctionWriteRepository.save(auction);
    }

    private BigDecimal getDifferenceBetweenBids(BigDecimal newBid, BigDecimal oldBid) {
        return oldBid.subtract(newBid);
    }

    private BigDecimal getAverageBid(List<Auction.Bid> bids) {
        BigDecimal a = bids.stream().map(Auction.Bid::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return a.divide(BigDecimal.valueOf(bids.size()), 1 , RoundingMode.HALF_UP);
    }

    private void validateBidAmount(BigDecimal amount, BigDecimal highestBidAmount) {
        if (amount.compareTo(highestBidAmount) <= 0) {
            throw new PlaceBidException("You cannot place less bid than your latest!");
        }
    }

    private void updateTeamBalance(Team team, BigDecimal bidAmountDiff) {
        if (team.getEconomy().getBalance().compareTo(bidAmountDiff.abs()) < 0) {
            throw new InsufficientBalanceException();
        }
        eventPublisher.publishEvent(BidEvent.builder().amount(bidAmountDiff)
            .teamId(team.getId().value()).build());
    }
}
