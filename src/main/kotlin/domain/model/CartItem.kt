package com.domain.model

import java.util.UUID

data class CartItem(
    val id: UUID,
    val cartId: UUID,
    val name: String,
    val price: Double,
    val quantity: Int
)
