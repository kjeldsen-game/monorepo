package com.kjeldsen.market.application;

import com.kjeldsen.lib.clients.PlayerClientApi;
import com.kjeldsen.lib.model.player.PlayerClient;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.market.utils.AuctionPlayerMapper;
import com.kjeldsen.player.domain.provider.InstantProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateAuctionUseCase {

    private final AuctionWriteRepository auctionWriteRepository;
    private final PlayerClientApi playerClientApi;
    private static final int AUCTION_LENGTH = 3;

    public Auction create(String playerId, String teamId) {
        log.info("CreateAuctionUseCase for player {} team {}", playerId, teamId);
        PlayerClient player = playerClientApi.getPlayer(playerId);

        if (player == null) {
            throw new IllegalArgumentException();
        }

        Auction auction = Auction.builder()
            .id(Auction.AuctionId.generate())
            .player(AuctionPlayerMapper.INSTANCE.map(player))
            .averageBid(BigDecimal.ZERO)
            .teamId(teamId)
            .bids(new ArrayList<>())
            .status(Auction.AuctionStatus.ACTIVE)
            .startedAt(InstantProvider.now())
            .endedAt(InstantProvider.nowPlusDays(AUCTION_LENGTH))
            .build();

        return auctionWriteRepository.save(auction);
    }
}
