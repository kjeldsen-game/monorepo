package com.kjeldsen.player.integration;

import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EmbeddedKafka(partitions = 1,
    topics = {
        "test-topic",
        "${kafka.topics.auth-signup}",
    })
@TestPropertySource(properties = "kafka.autostart=true")
public @interface WithEmbeddedKafka {
}
