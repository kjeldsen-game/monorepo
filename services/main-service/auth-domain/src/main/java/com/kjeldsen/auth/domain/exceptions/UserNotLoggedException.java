package com.kjeldsen.auth.domain.exceptions;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException() {
        super("User not logged in!");
    }
}
