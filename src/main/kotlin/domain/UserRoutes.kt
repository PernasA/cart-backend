package com.domain

import com.model.user.CreateUserRequest
import com.model.user.CreateUserResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.userRoutes() {

    route("/users") {

        post {
            val request = call.receive<CreateUserRequest>()

            val userId = UserRepository.createOrGetUser(
                firebaseUid = request.firebaseUid,
                email = request.email,
                displayName = request.displayName
            )

            call.respond(
                HttpStatusCode.Created,
                CreateUserResponse(id = userId.toString())
            )
        }
    }
}
