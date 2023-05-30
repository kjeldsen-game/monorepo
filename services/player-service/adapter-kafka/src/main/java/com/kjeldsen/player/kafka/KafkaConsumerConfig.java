package com.kjeldsen.player.kafka;

import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>>
    signUpKafkaConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> consumerProperties = consumerProperties(kafkaProperties);
        return new DefaultKafkaConsumerFactory<>(
            consumerProperties,
            new StringDeserializer(),
            new JsonDeserializer<>(KafkaEventWrapper.class, false));
    }

    public static Map<String, Object> consumerProperties(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

}
