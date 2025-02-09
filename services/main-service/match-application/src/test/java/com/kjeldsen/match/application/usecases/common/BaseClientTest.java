package com.kjeldsen.match.application.usecases.common;

import com.kjeldsen.auth.authorization.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public abstract class BaseClientTest {
    protected MockedStatic<SecurityUtils> mockedSecurityUtils;

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserToken).thenReturn("token");
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }
}
