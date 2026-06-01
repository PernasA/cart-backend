package com.application.cart

import com.domain.model.Cart
import com.domain.repository.CartRepository
import com.domain.repository.UserRepository

class GetUserCartsUseCase(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository
) {
    fun execute(firebaseUid: String): List<Cart> {
        val user = userRepository.createOrGet(
            firebaseUid = firebaseUid,
            email = null,
            displayName = null
        )
        return cartRepository.findByUserId(user.id)
    }
}
