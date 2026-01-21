package com.auth

import io.ktor.server.auth.Principal

data class FirebasePrincipal(
    val uid: String,
    val email: String?
) : Principal