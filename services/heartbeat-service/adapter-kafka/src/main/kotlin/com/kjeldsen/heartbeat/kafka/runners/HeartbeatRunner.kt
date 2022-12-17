package com.kjeldsen.heartbeat.kafka.runners

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate

@Bean
fun runner(template: KafkaTemplate<String?, String?>): ApplicationRunner {
    return ApplicationRunner {
        template.send("heartbeat-service_heartbeat", "test")
    }
}