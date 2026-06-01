package com.application.stats

import com.domain.repository.CartRepository
import com.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetStatsUseCaseTest {

    @Test
    fun `execute returns correct stats with normal data`() {
        val cartRepository = mockk<CartRepository>()
        val userRepository = mockk<UserRepository>()
        val useCase = GetStatsUseCase(cartRepository, userRepository)

        val topUserId = UUID.randomUUID()
        every { userRepository.countAll() } returns 5L
        every { cartRepository.countAll() } returns 10L
        every { cartRepository.topUserByCartCount() } returns Pair(topUserId, 3L)

        val result = useCase.execute()

        assertEquals(5L, result.totalUsers)
        assertEquals(10L, result.totalCarts)
        assertEquals(topUserId.toString(), result.topUserId)
        assertEquals(30.0, result.topUserPercentage)
    }

    @Test
    fun `execute returns null topUserPercentage when there are no carts`() {
        val cartRepository = mockk<CartRepository>()
        val userRepository = mockk<UserRepository>()
        val useCase = GetStatsUseCase(cartRepository, userRepository)

        val topUserId = UUID.randomUUID()
        every { userRepository.countAll() } returns 3L
        every { cartRepository.countAll() } returns 0L
        every { cartRepository.topUserByCartCount() } returns Pair(topUserId, 0L)

        val result = useCase.execute()

        assertEquals(0L, result.totalCarts)
        assertNull(result.topUserPercentage)
    }

    @Test
    fun `execute returns null topUserId and null topUserPercentage when topUserByCartCount returns null`() {
        val cartRepository = mockk<CartRepository>()
        val userRepository = mockk<UserRepository>()
        val useCase = GetStatsUseCase(cartRepository, userRepository)

        every { userRepository.countAll() } returns 2L
        every { cartRepository.countAll() } returns 5L
        every { cartRepository.topUserByCartCount() } returns null

        val result = useCase.execute()

        assertEquals(2L, result.totalUsers)
        assertEquals(5L, result.totalCarts)
        assertNull(result.topUserId)
        assertNull(result.topUserPercentage)
    }
}
