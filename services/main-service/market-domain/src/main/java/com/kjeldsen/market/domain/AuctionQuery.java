package com.kjeldsen.market.domain;

import com.kjeldsen.player.domain.Team;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AuctionQuery {
    private Auction.AuctionId auctionId;
    private BigDecimal averageBid;
    private Team.TeamId teamId;
    //private int size;
    //private int page;
}
