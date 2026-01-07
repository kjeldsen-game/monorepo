package com.kjeldsen.auth.domain;

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
