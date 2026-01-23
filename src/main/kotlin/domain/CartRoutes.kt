package com.domain

import com.domain.CartRepository.getCart
import com.model.cart.CreateCartRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.cartRoutes() {
    route("/cart") {

        get("/ping") {
            call.respondText("cart ok")
        }

        get("/{id}") {
            val idParam = call.parameters["id"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing cart id"
                )

            val cartId = runCatching { UUID.fromString(idParam) }
                .getOrElse {
                    return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Invalid cart id"
                    )
                }

            val cart = getCart(cartId)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(cart)
        }

        post {
            val request = call.receive<CreateCartRequest>()

            // ⚠ hardcodeado SOLO para local
            val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

            val cartId = CartRepository.createCart(
                userId = userId,
                name = request.name,
                createdAt = request.createdAt,
                items = request.items
            )

            call.respond(
                HttpStatusCode.Created,
                mapOf("id" to cartId.toString())
            )
        }

    }
}
