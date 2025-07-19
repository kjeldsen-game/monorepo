package com.kjeldsen.auth.application.usecases.email;

import com.kjeldsen.auth.application.usecases.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.mockito.Mockito.*;

class SendEmailUseCaseTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private SendEmailUseCase sendEmailUseCase;

    @Captor
    ArgumentCaptor<SimpleMailMessage> simpleMessageCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sendEmailUseCase = new SendEmailUseCase(javaMailSender);
        TestUtils.setField(sendEmailUseCase, "EMAIL_SENDER_USERNAME", "noreply@example.com");
    }

    @Test
    void sendSimpleEmail_shouldSendEmail() {
        sendEmailUseCase.sendSimpleEmail("user@example.com", "Test Subject", "Test Body");

        verify(javaMailSender, times(1)).send(simpleMessageCaptor.capture());
        SimpleMailMessage message = simpleMessageCaptor.getValue();

        assert Objects.requireNonNull(message.getTo())[0].equals("user@example.com");
        assert Objects.equals(message.getSubject(), "Test Subject");
        assert Objects.equals(message.getText(), "Test Body");
        assert Objects.equals(message.getFrom(), "noreply@example.com");
    }
}
