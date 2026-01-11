package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.application.usecases.email.SendEmailUseCase;
import com.kjeldsen.auth.domain.models.PasswordResetToken;
import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeneratePasswordResetLinkUseCase {

    private static final String RESET_URL = "http://localhost:3000/auth/reset-password";
    private final SendEmailUseCase sendEmailUseCase;
    private final GetUserUseCase getUserUseCase;
    private final PasswordResetTokenWriteRepository passwordResetTokenWriteRepository;

    public void generate(String email) {
        log.info("GeneratePasswordResetLinkUseCase for email={}", email);
        User user = getUserUseCase.getUserByEmail(email);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
            .token(UUID.randomUUID().toString())
            .userId(user.getId())
            .expiryDate(Instant.now().plus(1, ChronoUnit.HOURS))
            .build();

        passwordResetTokenWriteRepository.save(passwordResetToken);
        String resetUrl = createResetLink(passwordResetToken.getToken());
        sendEmailUseCase.sendSimpleEmail(email, "Password Reset Link", createEmailBody(resetUrl));
    }

    private String createResetLink(String token) {
        return RESET_URL + "?token=" + token;
    }

    private String createEmailBody(String link) {
        return "Hi,\n\n" +
            "We received a request to reset your password. To reset your password, please click the link below:\n\n" +
            link + "\n\n" +
            "If you did not request a password reset, please ignore this email. Your password will remain unchanged.\n\n" +
            "Thanks,\n" +
            "Your Kjeldsen Team";
    }
}
