package com.kjeldsen.heartbeat.scheduling.schedulers

import com.kjeldsen.heartbeat.kafka.producers.send
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HeartbeatScheduler {

    @Scheduled(fixedDelay = 5000)
    fun scheduleFixedDelayTask() {
        println(
            "Fixed delay task - " + System.currentTimeMillis() / 1000
        )
        send("Hi!")
    }
}