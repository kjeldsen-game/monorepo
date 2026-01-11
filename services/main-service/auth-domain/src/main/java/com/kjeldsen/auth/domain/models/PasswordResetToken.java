package com.kjeldsen.auth.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@Document(collection = "PasswordResetTokens")
public class PasswordResetToken {

    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private String token = UUID.randomUUID().toString();
    private String userId;
    @Builder.Default
    private Instant expiryDate = Instant.now().plus(1, ChronoUnit.HOURS);
}
