package com.model.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    val totalUsers: Long,
    val totalCarts: Long,
    val topUserId: String?,
    val topUserPercentage: Double?
)
