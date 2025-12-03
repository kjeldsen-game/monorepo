package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.player.domain.provider.InstantProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateAuctionUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private static final int AUCTION_LENGTH = 3;

    public Auction create(String playerId, String teamId) {
        log.info("CreateAuctionUseCase for player {} team {}", playerId, teamId);

        Auction auction = Auction.builder()
            .id(Auction.AuctionId.generate())
            .playerId(playerId)
            .averageBid(BigDecimal.ONE)
            .teamId(teamId)
            .bids(new ArrayList<>(List.of(
                Auction.Bid.builder()
                .amount(BigDecimal.ONE)
                .teamId(teamId)
                .timestamp(InstantProvider.now())
                .build()
            )))
            .status(Auction.AuctionStatus.ACTIVE)
            .startedAt(InstantProvider.now())
            .endedAt(InstantProvider.nowPlusDays(AUCTION_LENGTH))
            .build();

        return auctionWriteRepository.save(auction);
    }
}
