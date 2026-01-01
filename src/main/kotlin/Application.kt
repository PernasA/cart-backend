package com

import com.db.DatabaseFactory
import com.plugins.configureRouting
import com.plugins.configureSerialization
import com.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    configureSerialization()
    configureRouting()
    configureStatusPages()
}
