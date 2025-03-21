package com.kjeldsen.player.domain.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("You don't have enough balance to place bid!");
    }
}
