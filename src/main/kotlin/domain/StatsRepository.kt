package com.domain

import com.db.CartsTable
import com.db.UsersTable
import com.model.stats.StatsResponse
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object StatsRepository {

    fun getStats(): StatsResponse = transaction {

        val totalUsers = UsersTable.selectAll().count()
        val totalCarts = CartsTable.selectAll().count()

        val topUser = CartsTable
            .slice(CartsTable.userId, CartsTable.userId.count())
            .selectAll()
            .groupBy(CartsTable.userId)
            .orderBy(CartsTable.userId.count(), SortOrder.DESC)
            .limit(1)
            .firstOrNull()

        val topUserId = topUser?.get(CartsTable.userId)?.toString()
        val topUserCount = topUser?.get(CartsTable.userId.count())?.toLong() ?: 0L
        val topUserPercentage = if (totalCarts > 0) {
            topUserCount * 100.0 / totalCarts
        } else null

        StatsResponse(
            totalUsers = totalUsers,
            totalCarts = totalCarts,
            topUserId = topUserId,
            topUserPercentage = topUserPercentage
        )
    }
}
