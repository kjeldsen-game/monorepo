package com.kjeldsen.player.kafka;

import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>> userSignedUpEventKafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(userSignedUpEventProducerFactory(kafkaProperties));
    }

    @Bean
    public ProducerFactory<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>> userSignedUpEventProducerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigProperties(kafkaProperties), new StringSerializer(), jsonSerializer());
    }

    public Map<String, Object> producerConfigProperties(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    private <E> JsonSerializer<E> jsonSerializer() {
        return new JsonSerializer<>();
    }

}
