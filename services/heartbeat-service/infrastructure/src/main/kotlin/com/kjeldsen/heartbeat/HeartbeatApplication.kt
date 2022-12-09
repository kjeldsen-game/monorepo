package com.kjeldsen.heartbeat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeartbeatApplication

fun main(args: Array<String>) {
	runApplication<HeartbeatApplication>(*args)
}
