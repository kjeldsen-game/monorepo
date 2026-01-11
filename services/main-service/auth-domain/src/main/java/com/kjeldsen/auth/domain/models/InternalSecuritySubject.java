package com.kjeldsen.auth.domain.models;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum InternalSecuritySubject {
    PLAYER,
    TEAM,
    MATCH,
    MARKET,
    AUTH;

    /**
     * Verifies if the provided value is a valid InternalSecuritySubject.
     *
     * @param value the value to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String value) {
        log.debug("Validating InternalSecuritySubject: {}", value);
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            InternalSecuritySubject.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
