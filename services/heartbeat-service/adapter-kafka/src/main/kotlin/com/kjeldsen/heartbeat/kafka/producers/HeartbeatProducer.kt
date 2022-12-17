package com.kjeldsen.heartbeat.kafka.producers

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Bean
fun producerFactory(): ProducerFactory<String?, String?> {
    return DefaultKafkaProducerFactory(producerConfigs())
}

@Bean
fun producerConfigs(): Map<String, Any> {
    val props: MutableMap<String, Any> = HashMap()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    return props
}

@Bean
fun kafkaTemplate(): KafkaTemplate<String?, String?> {
    return KafkaTemplate(producerFactory())
}

fun send(message: String) {
    kafkaTemplate().send("heartbeat-service_heartbeat", message);
}