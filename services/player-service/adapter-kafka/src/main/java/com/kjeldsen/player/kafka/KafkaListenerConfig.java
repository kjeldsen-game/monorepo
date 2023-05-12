package com.kjeldsen.player.kafka;

import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaListenerConfig {

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>>>
    signUpKafkaListenerContainerFactory(ConsumerFactory<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
