package com.kjeldsen.auth.domain.repositories;

import com.kjeldsen.auth.domain.models.PasswordResetToken;

public interface PasswordResetTokenWriteRepository {

    PasswordResetToken save(PasswordResetToken passwordResetToken);

    void delete(final String token);
}
