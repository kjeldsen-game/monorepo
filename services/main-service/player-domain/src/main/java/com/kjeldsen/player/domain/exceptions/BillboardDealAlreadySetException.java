package com.kjeldsen.player.domain.exceptions;

public class BillboardDealAlreadySetException extends RuntimeException {
    public BillboardDealAlreadySetException() {
        super("Billboard deal is already set!");
    }
}
