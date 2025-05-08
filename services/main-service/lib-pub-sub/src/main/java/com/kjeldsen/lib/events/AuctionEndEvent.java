package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@ToString
public class AuctionEndEvent extends Event {
    String playerId;
    BigDecimal amount;
    String auctionWinner;
    String auctionCreator;
}
