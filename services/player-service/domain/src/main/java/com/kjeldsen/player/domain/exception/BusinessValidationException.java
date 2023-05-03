package com.kjeldsen.player.domain.exception;

public class BusinessValidationException extends RuntimeException {

    public static void throwIf(boolean condition, String errorMessage) {
        if (condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void throwIfNot(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
