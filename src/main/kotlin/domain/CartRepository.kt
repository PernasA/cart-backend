package com.domain

import com.db.CartItemsTable
import com.db.CartsTable
import com.domain.exception.UserNotFoundException
import com.model.cart.CartItemRequest
import com.model.cart.CartItemResponse
import com.model.cart.CartResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.collections.forEach
import kotlin.collections.sumOf

object CartRepository {

    fun createCart(
        userId: UUID,
        name: String,
        createdAt: Long,
        items: List<CartItemRequest>
    ): UUID =
        transaction {

            if (!UserRepository.exists(userId)) {
                throw UserNotFoundException()
            }

            val cartId = CartsTable.insertAndGetId {
                it[CartsTable.userId] = userId
                it[CartsTable.name] = name
                it[CartsTable.createdAt] = Instant.ofEpochMilli(createdAt)
                it[syncStatus] = "CREATED"

            }.value

            items.forEach { item ->
                CartItemsTable.insert {
                    it[CartItemsTable.cartId] = cartId
                    it[CartItemsTable.name] = item.name
                    it[CartItemsTable.price] = item.price.toBigDecimal()
                    it[CartItemsTable.quantity] = item.quantity
                }
            }

            cartId
        }

    fun calculateTotal(cartId: UUID): Double =
        transaction {
            CartItemsTable
                .selectAll().where { CartItemsTable.cartId eq cartId }
                .sumOf {
                    it[CartItemsTable.price] * it[CartItemsTable.quantity].toBigDecimal()
                }.toDouble()
        }

    fun getCart(cartId: UUID): CartResponse? =
        transaction {
            val cartRow = CartsTable
                .select { CartsTable.id eq cartId }
                .singleOrNull()
                ?: return@transaction null

            val items = CartItemsTable
                .select { CartItemsTable.cartId eq cartId }
                .map {
                    CartItemResponse(
                        id = it[CartItemsTable.id],
                        name = it[CartItemsTable.name],
                        price = it[CartItemsTable.price],
                        quantity = it[CartItemsTable.quantity]
                    )
                }

            var accumulator = BigDecimal.ZERO
            items.forEach { item ->
                accumulator += item.price * item.quantity.toBigDecimal()
            }

            CartResponse(
                id = cartRow[CartsTable.id].value.toString(),
                name = cartRow[CartsTable.name] ?: "",
                createdAt = cartRow[CartsTable.createdAt].toEpochMilli(),
                total = accumulator.toDouble()
            )
        }

}
