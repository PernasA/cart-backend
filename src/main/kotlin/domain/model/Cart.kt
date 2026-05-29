package com.domain.model

import java.util.UUID

data class Cart(
    val id: UUID,
    val userId: UUID,
    val name: String?,
    val createdAt: Long,
    val items: List<CartItem>
)
