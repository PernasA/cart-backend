package com.auth

import com.google.firebase.auth.FirebaseAuth
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureFirebaseAuth() {
    install(Authentication) {
        bearer("firebase-auth") {
            authenticate { tokenCredential ->
                try {
                    val decodedToken = FirebaseAuth.getInstance()
                        .verifyIdToken(tokenCredential.token)

                    FirebasePrincipal(
                        uid = decodedToken.uid,
                        email = decodedToken.email
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
