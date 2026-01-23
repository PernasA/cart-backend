package com.security

import io.ktor.server.application.*
import io.ktor.util.AttributeKey

val UserKey = AttributeKey<String>("user-id")

fun ApplicationCall.ensureUser() {
    if (!attributes.contains(UserKey)) {
        throw IllegalStateException("User not authenticated")
    }
}
