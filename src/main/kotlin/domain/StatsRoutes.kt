package com.domain

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.statsRoutes() {

    authenticate("firebase-auth") {

        get("/stats") {
            val stats = StatsRepository.getStats()
            call.respond(stats)
        }
    }
}
