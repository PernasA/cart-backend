package com.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItemRequest(
    val name: String,
    val price: Double,
    val quantity: Int
)