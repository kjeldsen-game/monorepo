package com.kjeldsen.heartbeat.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaAdmin

@Bean
fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"))
