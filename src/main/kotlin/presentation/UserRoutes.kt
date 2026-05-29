package com.presentation

import com.application.user.CreateOrGetUserUseCase
import com.model.user.CreateUserRequest
import com.model.user.CreateUserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRoutes(createOrGetUserUseCase: CreateOrGetUserUseCase) {

    route("/users") {

        post {
            val request = call.receive<CreateUserRequest>()

            val user = createOrGetUserUseCase.execute(
                firebaseUid = request.firebaseUid,
                email = request.email,
                displayName = request.displayName
            )

            call.respond(
                HttpStatusCode.Created,
                CreateUserResponse(id = user.id.toString())
            )
        }
    }
}
