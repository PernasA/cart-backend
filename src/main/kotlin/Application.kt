package com

import com.auth.configureFirebaseAuth
import com.config.configureFirebase
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

    configureFirebase()        // 1. SDK Firebase (infra)
    configureFirebaseAuth()    // 2. Auth of Ktor
    configureSerialization()  // 3. JSON
    configureStatusPages()    // 4. errors
    configureRouting()        // 5. routes (with auth)
}
