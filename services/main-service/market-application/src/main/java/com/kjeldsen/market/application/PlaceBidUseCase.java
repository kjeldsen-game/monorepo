package com.kjeldsen.market.application;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.events.market.BidEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.builders.PlaceBidNotificationEventBuilder;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.InsufficientBalanceException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.exceptions.TeamNotFoundException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.model.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceBidUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final AuctionReadRepository auctionReadRepository;
    private final GenericEventPublisher eventPublisher;
    private final TeamClientApi teamClientApi;

    private static final Integer AUCTION_BID_REDUCE_TIME = 30;

    public Auction placeBid(Auction.AuctionId auctionId, BigDecimal amount, String userId) {
        log.info("PlaceBidUseCase for auction={} of amount={} from userId={}", auctionId, amount, userId);

        Auction auction = auctionReadRepository.findById(auctionId).orElseThrow(
            AuctionNotFoundException::new);

        TeamResponse team;
        List<TeamResponse> teams = teamClientApi.getTeams( null, userId);
        if (teams.isEmpty()) {
            throw new TeamNotFoundException();
        } else {
            team = teams.get(0);
        }

        if (InstantProvider.now().isAfter(auction.getEndedAt())) {
            throw new PlaceBidException("Cannot place new bid on ended auction!");
        }

        if (Objects.equals(auction.getTeamId(), team.getId())) {
            throw new PlaceBidException("Cannot place new bid on auction you created!");
        }

        // Retrieve the highest bid of the Team if exists
        Auction.Bid highestBid = auction.getBids().stream()
            .filter(bid -> bid.getTeamId().equals(team.getId()))
            .max(Comparator.comparing(Auction.Bid::getAmount))
            .orElse(null);

        if (highestBid != null) {
            validateBidAmount(amount, highestBid.getAmount());
        }
        updateTeamBalance(team, amount);
        Auction.Bid bid = Auction.Bid.builder()
            .timestamp(InstantProvider.now())
            .teamId(team.getId())
            .amount(amount)
            .build();
        auction.getBids().add(bid);


        auction.reduceEndedAtBySeconds(AUCTION_BID_REDUCE_TIME);
        auction.setAverageBid(getAverageBid(auction.getBids()));

        // Filter Teams except the Owner and Current bidder to send a notification about new bid
        List<String> teamIds = auction.getBids().stream()
            .map(Auction.Bid::getTeamId)
            .filter(bidTeamId -> {
                String auctionOwnerId = auction.getTeamId();
                String currentBidderId = team.getId();
                return !bidTeamId.equals(auctionOwnerId) && !bidTeamId.equals(currentBidderId);
            })
            .distinct()
            .toList();

        if (!teamIds.isEmpty()) {
            eventPublisher.publishEvent(
                PlaceBidNotificationEventBuilder.build(teamIds, auction.getPlayerId())
            );
        }
        return auctionWriteRepository.save(auction);
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

    private void updateTeamBalance(TeamResponse team, BigDecimal bidAmount) {
        if (team.getEconomy().getBalance().compareTo(bidAmount.abs().doubleValue()) < 0) {
            throw new InsufficientBalanceException();
        }
        eventPublisher.publishEvent(BidEvent.builder().amount(bidAmount)
            .teamId(team.getId()).build());
    }
}
