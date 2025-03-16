package com.kjeldsen.market.application;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.exceptions.AuctionNotFoundException;
import com.kjeldsen.market.domain.exceptions.PlaceBidException;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEndUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final AuctionReadRepository auctionReadRepository;
    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;

    public AuctionEndEvent endAuction(Auction.AuctionId auctionId) {
        log.info("AuctionEndUseCase for auction {}", auctionId );

        Auction auction = auctionReadRepository.findById(auctionId).orElseThrow(
            AuctionNotFoundException::new);

        Auction.Bid highestBid = auction.getBids().stream()
            .max(Comparator.comparing(Auction.Bid::getAmount)).orElseThrow(
            () -> new PlaceBidException("Auction bid not found!"));

        // Highest bid is from Team that put player on Market -> take him back to the Team
        if (!highestBid.getTeamId().equals(auction.getTeamId())) {
            returnMoneyToNotWinningTeams(auction.getBids());
        }

        auction.setStatus(Auction.AuctionStatus.COMPLETED);
        auctionWriteRepository.save(auction);

        return AuctionEndEvent.builder()
            .id(EventId.generate())
            .occurredAt(Instant.now())
            .auctionWinner(highestBid.getTeamId())
            .auctionCreator(auction.getTeamId())
            .amount(highestBid.getAmount())
            .playerId(auction.getPlayerId())
            .build();
    }

    private void returnMoneyToNotWinningTeams(List<Auction.Bid> bids) {
        List<Auction.Bid> highestBidsPerTeam =  bids.stream()
            .collect(Collectors.groupingBy(
                Auction.Bid::getTeamId,
                Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparing(Auction.Bid::getAmount)),
                    optionalBid -> optionalBid.orElse(null)
                )
            ))
            .values().stream()
            .filter(Objects::nonNull).toList();

        for (Auction.Bid bid : highestBidsPerTeam) {
            try {
                Team team = teamReadRepository.findById(bid.getTeamId()).orElseThrow(
                    () -> new RuntimeException("Team not found"));
                team.getEconomy().updateBalance(bid.getAmount());
                teamWriteRepository.save(team);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
            }
        }
    }
}
