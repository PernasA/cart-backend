package com.model.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val firebaseUid: String,
    val email: String? = null,
    val displayName: String? = null
)
