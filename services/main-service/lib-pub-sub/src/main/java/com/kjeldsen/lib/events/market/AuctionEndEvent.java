package com.kjeldsen.lib.events.market;

import com.kjeldsen.lib.events.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@ToString
public class AuctionEndEvent extends Event {
    String playerId;
    BigDecimal amount;
    String auctionWinner;
    String auctionCreator;
    Map<String, BigDecimal> bidders;
}
