package com.kjeldsen.market.domain.repositories;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AuctionReadRepository {
    Optional<Auction> findById(Auction.AuctionId auctionId);

    List<Auction> findAll();

    List<Auction> findAllByQuery(FindAuctionsQuery query);

    Page<Auction> findAllByQueryPaged(FindAuctionsQuery query);
}
