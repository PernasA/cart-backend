package com.plugins

import com.domain.exception.UserNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlin.collections.mapOf

fun Application.configureStatusPages() {
    install(StatusPages) {

        exception<UserNotFoundException> { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to "User not found")
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to cause.message)
            )
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled error", cause)
            cause.printStackTrace()

            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "Internal server error")
            )
        }
    }
}
