package com.kjeldsen.lib.publishers;

public interface GenericEventPublisher {

    <T> void publishEvent(T event);
}
