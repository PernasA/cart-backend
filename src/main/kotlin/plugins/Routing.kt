package com.plugins


import com.auth.FirebasePrincipal
import com.domain.cartRoutes
import com.domain.statsRoutes
import com.domain.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*

fun Application.configureRouting() {
    log.info("CONFIGURE ROUTING EJECUTADO")
    routing {
        get("/") {
            call.respondText("root ok")
        }

        authenticate("firebase-auth") {
            get("/me") {
                val principal = call.principal<FirebasePrincipal>()
                    ?: error("No principal")

                call.respond(
                    mapOf(
                        "uid" to principal.uid,
                        "email" to principal.email
                    )
                )
            }
        }

        cartRoutes()
        userRoutes()
        statsRoutes()
    }
}
