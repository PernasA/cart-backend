package com.presentation

import com.application.stats.GetStatsUseCase
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.statsRoutes(getStatsUseCase: GetStatsUseCase) {

    authenticate("firebase-auth") {

        get("/stats") {
            val stats = getStatsUseCase.execute()
            call.respond(stats)
        }
    }
}
