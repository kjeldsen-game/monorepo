package com.kjeldsen.match.controllers;

public class ValidationException extends RuntimeException {

    /*
     * Generic exception for failed validation during model creation and updates
     */

    public ValidationException(String message) {
        super(message);
    }
}
