package com.kjeldsen.auth.domain.repositories;

import com.kjeldsen.auth.domain.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenReadRepository {

    Optional<PasswordResetToken> findByToken(String token);
}

