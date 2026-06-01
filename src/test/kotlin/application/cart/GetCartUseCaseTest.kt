package com.application.cart

import com.domain.model.Cart
import com.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetCartUseCaseTest {

    @Test
    fun `execute returns cart when it exists`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = GetCartUseCase(cartRepository)

        val cartId = UUID.randomUUID()
        val expectedCart = Cart(
            id = cartId,
            userId = UUID.randomUUID(),
            name = "Test Cart",
            createdAt = 1_700_000_000L,
            items = emptyList()
        )

        every { cartRepository.findById(cartId) } returns expectedCart

        val result = useCase.execute(cartId)

        assertNotNull(result)
        assertEquals(expectedCart, result)
    }

    @Test
    fun `execute returns null when cart does not exist`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = GetCartUseCase(cartRepository)

        val cartId = UUID.randomUUID()

        every { cartRepository.findById(cartId) } returns null

        val result = useCase.execute(cartId)

        assertNull(result)
    }

    @Test
    fun `execute calls cartRepository findById exactly once with the given cartId`() {
        val cartRepository = mockk<CartRepository>()
        val useCase = GetCartUseCase(cartRepository)

        val cartId = UUID.randomUUID()

        every { cartRepository.findById(cartId) } returns null

        useCase.execute(cartId)

        verify(exactly = 1) { cartRepository.findById(cartId) }
    }
}
