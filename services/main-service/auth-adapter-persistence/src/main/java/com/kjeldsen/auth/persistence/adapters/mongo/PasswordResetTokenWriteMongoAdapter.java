package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.models.PasswordResetToken;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenWriteRepository;
import com.kjeldsen.auth.persistence.mongo.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenWriteMongoAdapter implements PasswordResetTokenWriteRepository {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void delete(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }
}
