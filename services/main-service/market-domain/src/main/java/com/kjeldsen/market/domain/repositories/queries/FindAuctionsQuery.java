package com.kjeldsen.market.domain.repositories.queries;

import com.kjeldsen.market.domain.Auction;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindAuctionsQuery {

    Auction.AuctionStatus auctionStatus;
    Double minAverageBid;
    Double maxAverageBid;
}
