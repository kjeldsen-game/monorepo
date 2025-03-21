package com.kjeldsen.player.domain.exceptions;

public class BillboardDealNotFoundException extends RuntimeException {

    public BillboardDealNotFoundException() {
        super("BillboardDeal not found!");
    }
}
