package com.application.stats

import com.domain.repository.CartRepository
import com.domain.repository.UserRepository
import com.model.stats.StatsResponse

class GetStatsUseCase(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) {

    fun execute(): StatsResponse {
        val totalUsers = userRepository.countAll()
        val totalCarts = cartRepository.countAll()

        val topUserPair = cartRepository.topUserByCartCount()
        val topUserId = topUserPair?.first?.toString()
        val topUserCount = topUserPair?.second ?: 0L
        val topUserPercentage = if (totalCarts > 0 && topUserPair != null) {
            topUserCount * 100.0 / totalCarts
        } else null

        return StatsResponse(
            totalUsers = totalUsers,
            totalCarts = totalCarts,
            topUserId = topUserId,
            topUserPercentage = topUserPercentage
        )
    }
}
