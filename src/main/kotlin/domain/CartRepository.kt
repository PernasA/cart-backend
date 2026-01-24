package com.domain

import com.db.CartItemsTable
import com.db.CartsTable
import com.model.cart.CartItemResponse
import com.model.cart.CartWithItemsResponse
import com.model.cart.CreateCartRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.collections.forEach

object CartRepository {

    fun createCart(
        userId: UUID,
        request: CreateCartRequest
    ): CartWithItemsResponse = transaction {

        val cartId = UUID.randomUUID()

        CartsTable.insert {
            it[CartsTable.id] = cartId
            it[CartsTable.userId] = userId
            it[CartsTable.name] = request.name
            it[CartsTable.createdAt] = request.createdAt
        }

        request.items.forEach { item ->
            CartItemsTable.insert {
                it[CartItemsTable.id] = UUID.randomUUID()
                it[CartItemsTable.cartId] = cartId   // ← FIX
                it[CartItemsTable.name] = item.name
                it[CartItemsTable.price] = item.price
                it[CartItemsTable.quantity] = item.quantity
            }
        }

        buildCartResponse(cartId)
    }

    fun getCart(cartId: UUID): CartWithItemsResponse? =
        transaction {
            if (
                CartsTable
                    .select { CartsTable.id eq cartId }
                    .empty()
            ) return@transaction null

            buildCartResponse(cartId)
        }

    private fun buildCartResponse(cartId: UUID): CartWithItemsResponse {
        val cartRow = CartsTable
            .select { CartsTable.id eq cartId }
            .single()

        val items = CartItemsTable
            .select { CartItemsTable.cartId eq cartId }
            .map {
                CartItemResponse(
                    id = it[CartItemsTable.id].toString(),
                    name = it[CartItemsTable.name],
                    price = it[CartItemsTable.price],
                    quantity = it[CartItemsTable.quantity]
                )
            }

        return CartWithItemsResponse(
            id = cartRow[CartsTable.id].toString(),
            userId = cartRow[CartsTable.userId].toString(),
            name = cartRow[CartsTable.name],
            createdAt = cartRow[CartsTable.createdAt].toString(),
            items = items
        )
    }
}

