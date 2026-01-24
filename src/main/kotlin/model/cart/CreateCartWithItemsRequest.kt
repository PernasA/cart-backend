package com.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class CreateCartWithItemsRequest(
    val name: String,
    val items: List<CartItemRequest>
)