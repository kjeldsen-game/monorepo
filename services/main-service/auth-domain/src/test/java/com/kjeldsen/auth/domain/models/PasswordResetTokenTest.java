package com.kjeldsen.auth.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {


    @Test
    @DisplayName("Should build PasswordResetToken object prefilled")
    void should_build_PasswordResetToken_object_prefilled() {
        PasswordResetToken token = PasswordResetToken.builder().build();
        assertNotNull(token);
        assertNotNull(token.getToken());
        assertNotNull(token.getExpiryDate());
        assertNotNull(token.getId());
    }
}