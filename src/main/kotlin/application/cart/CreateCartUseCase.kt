package com.application.cart

import com.domain.model.Cart
import com.domain.model.CartItem
import com.domain.repository.CartRepository
import java.util.UUID

data class CartItemInput(
    val name: String,
    val price: Double,
    val quantity: Int
)

class CreateCartUseCase(private val cartRepository: CartRepository) {

    fun execute(userId: UUID, name: String?, createdAt: Long, items: List<CartItemInput>): Cart {
        val domainItems = items.map { item ->
            CartItem(
                id = UUID.randomUUID(),
                cartId = UUID.randomUUID(), // placeholder; repository assigns the real cartId
                name = item.name,
                price = item.price,
                quantity = item.quantity
            )
        }
        return cartRepository.create(userId, name, createdAt, domainItems)
    }
}
