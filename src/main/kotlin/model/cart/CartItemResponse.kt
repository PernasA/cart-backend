package com.model.cart

import java.util.UUID
import java.math.BigDecimal
import kotlinx.serialization.Serializable
import com.model.serializer.UUIDSerializer
import com.model.serializer.BigDecimalSerializer

@Serializable
data class CartItemResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,

    val name: String,

    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,

    val quantity: Int
)

