package com.kjeldsen.integration.auth;

import com.icegreen.greenmail.util.GreenMail;
import com.kjeldsen.auth.application.usecases.email.SendEmailUseCase;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"test-it"})
public class SendEmailUseCaseIT {

    private static GreenMail greenMail;

    @Autowired
    SendEmailUseCase sendEmailUseCase;

    @Value("${spring.mail.username}")
    private String EMAIL_SENDER_USERNAME;
    @Value("${spring.mail.password}")
    private String EMAIL_SENDER_PASSWORD;

    @BeforeEach
    void startMailServer() {
        greenMail = new GreenMail();
        greenMail.start();
        greenMail.setUser(EMAIL_SENDER_USERNAME, EMAIL_SENDER_PASSWORD);
    }

    @AfterAll
    static void stopMailServer() {
        greenMail.stop();
    }

    @Test
    void shouldSendEmailSuccessfully() throws Exception {

        sendEmailUseCase.sendSimpleEmail(
            "receiver@test.com",
            "Test Subject",
            "Hello from integration test"
        );

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage message = receivedMessages[0];
        assertEquals("Test Subject", message.getSubject());
        assertEquals("Hello from integration test", message.getContent().toString().trim());
        assertEquals(EMAIL_SENDER_USERNAME, message.getFrom()[0].toString());
    }
}
