package com.kjeldsen.heartbeat.kafka.topic

import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.TopicBuilder

@Bean
fun heartbeatTopic() = TopicBuilder.name("heartbeat-service_heartbeat").build()
