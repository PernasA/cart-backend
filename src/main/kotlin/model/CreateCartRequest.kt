package com.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateCartRequest(
    val name: String,
    val createdAt: Long,
    val items: List<CartItemRequest>
)
