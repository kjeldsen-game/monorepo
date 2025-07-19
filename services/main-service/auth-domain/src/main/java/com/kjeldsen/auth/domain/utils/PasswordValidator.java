package com.kjeldsen.auth.domain.utils;

import com.kjeldsen.auth.domain.exceptions.BadRequestException;

public class PasswordValidator {

    public static void validatePassword(String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("Passwords do not match!");
        }
    }
}
