package com.kjeldsen.player.integration;

import com.kjeldsen.events.kafka.KafkaEventWrapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

public abstract class KafkaAbstractIT<T, V> extends AbstractIT {

    private static final Duration DEFAULT_POLL_DURATION = Duration.ofSeconds(5);

    @Autowired
    public KafkaTemplate<String, KafkaEventWrapper<T, V>> kafkaTemplate;

    @Autowired
    public ConsumerFactory<String, KafkaEventWrapper<T, V>> consumerFactory;

    public KafkaConsumer<String, KafkaEventWrapper<T, V>> consumer;

    @BeforeEach
    void beforeEachSetup() {
        consumer = (KafkaConsumer<String, KafkaEventWrapper<T, V>>) consumerFactory.createConsumer();
    }

    public void sendEvent(String topic, KafkaEventWrapper<T, V> kafkaEventWrapper) {
        kafkaTemplate.send(topic, kafkaEventWrapper);
    }

    public List<ConsumerRecord<String, KafkaEventWrapper<T, V>>> getEvents(String topic) {
        consumer.subscribe(Collections.singletonList(topic));
        return StreamSupport
            .stream(consumer.poll(DEFAULT_POLL_DURATION).spliterator(), false)
            .toList();
    }

    public <O> O convert(Object value, Class<O> clazz) {
        return objectMapper.convertValue(value, clazz);
    }

    @AfterEach
    void afterEachSetup() {
        consumer.unsubscribe();
        consumer.close();
    }

}

