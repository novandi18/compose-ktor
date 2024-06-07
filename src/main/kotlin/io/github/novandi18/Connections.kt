package io.github.novandi18

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connections(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}