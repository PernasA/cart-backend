package com

import com.db.DatabaseFactory
import com.infrastructure.firebase.configureFirebase
import com.infrastructure.firebase.configureSecurity
import com.plugins.configureRouting
import com.plugins.configureSerialization
import com.plugins.configureStatusPages
import io.ktor.server.application.*

fun Application.module() {
    DatabaseFactory.init()

    configureFirebase()         // 1. SDK Firebase (infra)
    configureSecurity()         // 2. Auth of Ktor
    configureSerialization()    // 3. JSON
    configureStatusPages()      // 4. errors
    configureRouting()          // 5. routes (with auth)
}
