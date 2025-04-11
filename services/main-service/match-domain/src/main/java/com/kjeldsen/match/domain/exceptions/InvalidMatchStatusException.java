package com.kjeldsen.match.domain.exceptions;

public class InvalidMatchStatusException extends RuntimeException {
    public InvalidMatchStatusException() {
        super("Invalid match status!");
    }
    public InvalidMatchStatusException(String message) {
      super(message);
    }
}
