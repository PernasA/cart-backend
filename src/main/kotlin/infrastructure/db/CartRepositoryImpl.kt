package com.infrastructure.db

import com.db.CartItemsTable
import com.db.CartsTable
import com.domain.model.Cart
import com.domain.model.CartItem
import com.domain.repository.CartRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CartRepositoryImpl : CartRepository {

    override fun create(userId: UUID, name: String?, createdAt: Long, items: List<CartItem>): Cart =
        transaction {
            val cartId = UUID.randomUUID()

            CartsTable.insert {
                it[CartsTable.id] = cartId
                it[CartsTable.userId] = userId
                it[CartsTable.name] = name
                it[CartsTable.createdAt] = createdAt
            }

            items.forEach { item ->
                CartItemsTable.insert {
                    it[CartItemsTable.id] = UUID.randomUUID()
                    it[CartItemsTable.cartId] = cartId
                    it[CartItemsTable.name] = item.name
                    it[CartItemsTable.price] = item.price
                    it[CartItemsTable.quantity] = item.quantity
                }
            }

            buildCart(cartId)
        }

    override fun findById(cartId: UUID): Cart? =
        transaction {
            if (CartsTable.select { CartsTable.id eq cartId }.empty()) {
                return@transaction null
            }
            buildCart(cartId)
        }

    override fun findByUserId(userId: UUID): List<Cart> =
        transaction {
            CartsTable
                .select { CartsTable.userId eq userId }
                .map { row -> buildCart(row[CartsTable.id]) }
        }

    override fun countAll(): Long =
        transaction {
            CartsTable.selectAll().count()
        }

    override fun topUserByCartCount(): Pair<UUID, Long>? =
        transaction {
            CartsTable
                .slice(CartsTable.userId, CartsTable.userId.count())
                .selectAll()
                .groupBy(CartsTable.userId)
                .orderBy(CartsTable.userId.count(), SortOrder.DESC)
                .limit(1)
                .firstOrNull()
                ?.let { row ->
                    val userId = row[CartsTable.userId]
                    val count = row[CartsTable.userId.count()]
                    Pair(userId, count)
                }
        }

    private fun buildCart(cartId: UUID): Cart {
        val cartRow = CartsTable
            .select { CartsTable.id eq cartId }
            .single()

        val items = CartItemsTable
            .select { CartItemsTable.cartId eq cartId }
            .map { row ->
                CartItem(
                    id = row[CartItemsTable.id],
                    cartId = cartId,
                    name = row[CartItemsTable.name],
                    price = row[CartItemsTable.price],
                    quantity = row[CartItemsTable.quantity]
                )
            }

        return Cart(
            id = cartRow[CartsTable.id],
            userId = cartRow[CartsTable.userId],
            name = cartRow[CartsTable.name],
            createdAt = cartRow[CartsTable.createdAt],
            items = items
        )
    }
}
