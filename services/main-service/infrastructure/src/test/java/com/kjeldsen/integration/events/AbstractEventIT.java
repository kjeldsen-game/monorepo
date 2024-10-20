package com.kjeldsen.integration.events;


import com.kjeldsen.integration.TestApplication;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles({"test", "test-it"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractEventIT {

    @Autowired
    protected ApplicationEventPublisher testEventPublisher;


}
