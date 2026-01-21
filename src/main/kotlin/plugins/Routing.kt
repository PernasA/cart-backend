package com.plugins


import com.auth.FirebasePrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
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
    }
}
