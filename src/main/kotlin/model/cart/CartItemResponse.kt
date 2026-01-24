package com.model.cart

import java.util.UUID
import java.math.BigDecimal
import kotlinx.serialization.Serializable
import com.model.serializer.UUIDSerializer
import com.model.serializer.BigDecimalSerializer

@Serializable
data class CartItemResponse(
     val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

