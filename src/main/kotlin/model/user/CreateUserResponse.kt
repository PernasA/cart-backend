package com.model.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserResponse(
    val id: String
)
