package com.kjeldsen.auth.domain.utils;

import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    @DisplayName("Should throw error when passwords are not same")
    void should_throw_error_when_passwords_are_same() {
        assertEquals("Passwords do not match!", assertThrows(BadRequestException.class,
            () -> PasswordValidator.validatePassword("pass1", "pass2")).getMessage());
    }
}