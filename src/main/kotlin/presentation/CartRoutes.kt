package com.presentation

import com.application.cart.CartItemInput
import com.application.cart.CreateCartUseCase
import com.application.cart.GetCartUseCase
import com.application.cart.GetUserCartsUseCase
import com.infrastructure.firebase.FirebasePrincipal
import com.model.cart.CartItemResponse
import com.model.cart.CartWithItemsResponse
import com.model.cart.CreateCartRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.cartRoutes(
    createCartUseCase: CreateCartUseCase,
    getCartUseCase: GetCartUseCase,
    getUserCartsUseCase: GetUserCartsUseCase
) {
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

            val cart = getCartUseCase.execute(cartId)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            val response = CartWithItemsResponse(
                id = cart.id.toString(),
                userId = cart.userId.toString(),
                name = cart.name,
                createdAt = cart.createdAt.toString(),
                items = cart.items.map { item ->
                    CartItemResponse(
                        id = item.id.toString(),
                        name = item.name,
                        price = item.price,
                        quantity = item.quantity
                    )
                }
            )

            call.respond(response)
        }

        authenticate("firebase-auth") {
            get {
                val principal = call.principal<FirebasePrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val carts = getUserCartsUseCase.execute(principal.uid)

                val response = carts.map { cart ->
                    CartWithItemsResponse(
                        id = cart.id.toString(),
                        userId = cart.userId.toString(),
                        name = cart.name,
                        createdAt = cart.createdAt.toString(),
                        items = cart.items.map { item ->
                            CartItemResponse(
                                id = item.id.toString(),
                                name = item.name,
                                price = item.price,
                                quantity = item.quantity
                            )
                        }
                    )
                }

                call.respond(response)
            }
        }

        post {
            val request = call.receive<CreateCartRequest>()

            // hardcoded only for local testing
            val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

            val cart = createCartUseCase.execute(
                userId = userId,
                name = request.name,
                createdAt = request.createdAt,
                items = request.items.map { item ->
                    CartItemInput(
                        name = item.name,
                        price = item.price,
                        quantity = item.quantity
                    )
                }
            )

            call.respond(
                HttpStatusCode.Created,
                mapOf("id" to cart.id.toString())
            )
        }
    }
}
