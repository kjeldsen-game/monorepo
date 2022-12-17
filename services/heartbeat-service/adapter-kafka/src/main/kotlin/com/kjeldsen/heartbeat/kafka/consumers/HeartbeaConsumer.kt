package com.kjeldsen.heartbeat.kafka.consumers

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class HeartbeatConsumer {

    @KafkaListener(topics = ["heartbeat-service_heartbeat"], groupId = "heartbeat-service_group")
    fun listen(data: String) {
        println(
            "received - $data"
        )
    }

}
