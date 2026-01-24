package com.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartWithItemsResponse(
    val id: String,
    val userId: String,
    val name: String?,
    val createdAt: String,
    val items: List<CartItemResponse>
)