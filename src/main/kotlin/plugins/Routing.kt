package com.plugins

import com.application.cart.CreateCartUseCase
import com.application.cart.GetCartUseCase
import com.application.stats.GetStatsUseCase
import com.application.user.CreateOrGetUserUseCase
import com.infrastructure.db.CartRepositoryImpl
import com.infrastructure.db.UserRepositoryImpl
import com.infrastructure.firebase.FirebasePrincipal
import com.presentation.cartRoutes
import com.presentation.statsRoutes
import com.presentation.userRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    log.info("CONFIGURE ROUTING EJECUTADO")

    val cartRepo = CartRepositoryImpl()
    val userRepo = UserRepositoryImpl()

    val createCartUseCase = CreateCartUseCase(cartRepo)
    val getCartUseCase = GetCartUseCase(cartRepo)
    val createOrGetUserUseCase = CreateOrGetUserUseCase(userRepo)
    val getStatsUseCase = GetStatsUseCase(cartRepo, userRepo)

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

        cartRoutes(createCartUseCase, getCartUseCase)
        userRoutes(createOrGetUserUseCase)
        statsRoutes(getStatsUseCase)
    }
}
