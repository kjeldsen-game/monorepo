package com.kjeldsen.market.application;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class GetHistoryAuctionsUseCase {

    private final AuctionReadRepository auctionReadRepository;

    public Page<Auction> get(String teamId, Integer pageNumber, Integer pageSize) {
        log.info("GetMarketAuctionsUseCase for teamId = {} size={} number={}", teamId, pageSize, pageNumber);
        if (teamId == null) {
            throw new IllegalArgumentException("Team Id cannot be null");
        }
        return auctionReadRepository.findAllByQueryPaged(FindAuctionsQuery.builder().size(pageSize)
                .teamId(teamId).page(pageNumber).auctionStatus(List.of(Auction.AuctionStatus.COMPLETED,
                Auction.AuctionStatus.CANCELED)).build());
    }
}
