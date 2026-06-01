package com.application.cart

import com.domain.model.Cart
import com.domain.model.CartItem
import com.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateCartUseCaseTest {

    @Test
    fun `execute with items calls cartRepository create and returns the cart`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = CreateCartUseCase(cartRepository)

        val userId = UUID.randomUUID()
        val cartId = UUID.randomUUID()
        val name = "My Cart"
        val createdAt = 1_700_000_000L
        val items = listOf(
            CartItemInput(name = "Apple", price = 1.5, quantity = 2),
            CartItemInput(name = "Banana", price = 0.75, quantity = 3)
        )

        val expectedCart = Cart(
            id = cartId,
            userId = userId,
            name = name,
            createdAt = createdAt,
            items = listOf(
                CartItem(id = UUID.randomUUID(), cartId = cartId, name = "Apple", price = 1.5, quantity = 2),
                CartItem(id = UUID.randomUUID(), cartId = cartId, name = "Banana", price = 0.75, quantity = 3)
            )
        )

        every { cartRepository.create(userId, name, createdAt, any()) } returns expectedCart

        val result = useCase.execute(userId, name, createdAt, items)

        assertNotNull(result)
        assertEquals(expectedCart, result)
        verify(exactly = 1) { cartRepository.create(userId, name, createdAt, any()) }
    }

    @Test
    fun `execute with empty items list calls cartRepository create with empty domain items`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = CreateCartUseCase(cartRepository)

        val userId = UUID.randomUUID()
        val cartId = UUID.randomUUID()
        val name = "Empty Cart"
        val createdAt = 1_700_000_001L

        val expectedCart = Cart(
            id = cartId,
            userId = userId,
            name = name,
            createdAt = createdAt,
            items = emptyList()
        )

        every { cartRepository.create(userId, name, createdAt, emptyList()) } returns expectedCart

        val result = useCase.execute(userId, name, createdAt, emptyList())

        assertNotNull(result)
        assertEquals(emptyList(), result.items)
        verify(exactly = 1) { cartRepository.create(userId, name, createdAt, emptyList()) }
    }

    @Test
    fun `execute returns cart with the same userId, name and createdAt that were passed`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = CreateCartUseCase(cartRepository)

        val userId = UUID.randomUUID()
        val cartId = UUID.randomUUID()
        val name = "Special Cart"
        val createdAt = 9_999_999_999L

        val expectedCart = Cart(
            id = cartId,
            userId = userId,
            name = name,
            createdAt = createdAt,
            items = emptyList()
        )

        every { cartRepository.create(userId, name, createdAt, any()) } returns expectedCart

        val result = useCase.execute(userId, name, createdAt, emptyList())

        assertEquals(userId, result.userId)
        assertEquals(name, result.name)
        assertEquals(createdAt, result.createdAt)
    }
}
