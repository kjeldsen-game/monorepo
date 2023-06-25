package com.kjeldsen.player.integration.kafka;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.events.kafka.KafkaEventWrapper;
import com.kjeldsen.player.kafka.events.AuthKafkaEventType;
import com.kjeldsen.player.kafka.events.body.UserSignedUpEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleProducerAndConsumerIT extends KafkaAbstractIT<AuthKafkaEventType, UserSignedUpEvent> {

    private final String TOPIC = "test-topic";

    @Test
    void exampleProduceAndConsume() {

        // Crear el userSignedUpEvent con su ID.
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
            .id(EventId.generate())
            .build();

        // En el wrapper viene --> <Auth (USER_SIGNED_UP) y EVENTO (idUser + Team(?))> como BODY EVENTO USER y como TIPO el de AUTH

        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEventWrapper =
            KafkaEventWrapper.<AuthKafkaEventType, UserSignedUpEvent>builder()
                .eventType(AuthKafkaEventType.USER_SIGNED_UP)
                .eventBody(userSignedUpEvent)
                .build();

        // Envia al topico el Wrapper;

        sendEvent(TOPIC, kafkaEventWrapper);

        // Lista de un Registro(segun X tópico -> wrappers)
        List<ConsumerRecord<String, KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent>>> events = getEvents(TOPIC);

        // kafkaEvent --> evento entero
        KafkaEventWrapper<AuthKafkaEventType, UserSignedUpEvent> kafkaEvent = events.get(0).value();
        // kafkaEventType --> SOLO TIPO evento
        AuthKafkaEventType kafkaEventType = convert(kafkaEvent.getEventType(), AuthKafkaEventType.class);
        // kafkaEventBody --> SOLO Cuerpo evento (contiene user id + team)
        UserSignedUpEvent kafkaEventBody = convert(kafkaEvent.getEventBody(), UserSignedUpEvent.class);

        assertEquals(AuthKafkaEventType.USER_SIGNED_UP, kafkaEventType);
        assertEquals(userSignedUpEvent.getId(), kafkaEventBody.getId());
        assertEquals(userSignedUpEvent.getTeamName(), kafkaEventBody.getTeamName());

    }
}

