package com.domain.repository

import com.domain.model.Cart
import com.domain.model.CartItem
import java.util.UUID

interface CartRepository {
    fun create(userId: UUID, name: String?, createdAt: Long, items: List<CartItem>): Cart
    fun findById(cartId: UUID): Cart?
    fun findByUserId(userId: UUID): List<Cart>
    fun countAll(): Long
    fun topUserByCartCount(): Pair<UUID, Long>?
}
