package com.kjeldsen.market.domain.repositories.queries;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.player.domain.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FindAuctionsQuery {

    Auction.AuctionStatus auctionStatus;
    Double minAverageBid;
    Double maxAverageBid;
    Player.PlayerId playerId;
}
