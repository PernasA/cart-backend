package com.model

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val id: String,
    val name: String,
    val createdAt: Long,
    val total: Double
)
