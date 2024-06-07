package io.github.novandi18.plugins

import io.github.novandi18.Connections
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.lang.Exception
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val connections = Collections.synchronizedSet<Connections?>(LinkedHashSet())
        webSocket("/chat") {
            val connect = Connections(this)
            connections += connect
            try {
                send("There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${connect.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                        println(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $connect!")
                connections -= connect
            }
        }
    }
}
