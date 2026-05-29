package com.application.cart

import com.domain.model.Cart
import com.domain.repository.CartRepository
import java.util.UUID

class GetCartUseCase(private val cartRepository: CartRepository) {

    fun execute(cartId: UUID): Cart? {
        return cartRepository.findById(cartId)
    }
}
