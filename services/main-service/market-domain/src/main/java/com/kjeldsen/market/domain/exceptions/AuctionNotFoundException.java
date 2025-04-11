package com.kjeldsen.market.domain.exceptions;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException() {
        super("Auction not found!");
    }
}
