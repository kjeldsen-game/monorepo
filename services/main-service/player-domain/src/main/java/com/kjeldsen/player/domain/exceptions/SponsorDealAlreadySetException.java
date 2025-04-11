package com.kjeldsen.player.domain.exceptions;

public class SponsorDealAlreadySetException extends RuntimeException {
    public SponsorDealAlreadySetException() {
        super("Sponsor deal is already set!");
    }
}
