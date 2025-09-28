package com.kjeldsen.lib.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

class GenericEventPublisherImplTest {

    private ApplicationEventPublisher applicationEventPublisher;
    private GenericEventPublisherImpl genericEventPublisher;

    @BeforeEach
    void setUp() {
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        genericEventPublisher = new GenericEventPublisherImpl(applicationEventPublisher);
    }

    @Test
    void should_publish_event() {
        String event = "Test Event";
        genericEventPublisher.publishEvent(event);
        verify(applicationEventPublisher, times(1)).publishEvent(event);
    }
}
