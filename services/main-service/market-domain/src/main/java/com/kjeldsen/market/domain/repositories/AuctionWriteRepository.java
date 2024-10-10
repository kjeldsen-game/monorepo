package com.kjeldsen.market.domain.repositories;


import com.kjeldsen.market.domain.Auction;

public interface AuctionWriteRepository {
    Auction save(final Auction auction);
}
