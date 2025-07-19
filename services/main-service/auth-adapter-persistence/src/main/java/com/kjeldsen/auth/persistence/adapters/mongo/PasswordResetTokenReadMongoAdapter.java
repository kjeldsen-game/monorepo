package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.PasswordResetToken;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenReadRepository;
import com.kjeldsen.auth.persistence.mongo.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenReadMongoAdapter implements PasswordResetTokenReadRepository {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}
