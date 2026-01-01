package com.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : Table("users") {
    val id = uuid("id").autoGenerate()
    val firebaseUid = varchar("firebase_uid", 128).uniqueIndex()
    val email = varchar("email", 255).nullable()
    val displayName = varchar("display_name", 255).nullable()
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}

object DevicesTable : Table("devices") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(UsersTable.id)
    val deviceId = varchar("device_id", 128)
    val platform = varchar("platform", 50).nullable()
    val createdAt = timestamp("created_at")
    val lastSeenAt = timestamp("last_seen_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

object CartsTable : Table("carts") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id").references(UsersTable.id)
    val deviceId = uuid("device_id").references(DevicesTable.id).nullable()
    val name = varchar("name", 255).nullable()
    val clientCreatedAt = long("client_created_at").nullable()
    val createdAt = timestamp("created_at")
    val syncStatus = varchar("sync_status", 50)

    override val primaryKey = PrimaryKey(id)
}

object CartItemsTable : Table("cart_items") {
    val id = uuid("id").autoGenerate()
    val cartId = uuid("cart_id").references(CartsTable.id)
    val name = varchar("name", 255)
    val price = decimal("price", 12, 2)
    val quantity = integer("quantity")

    override val primaryKey = PrimaryKey(id)
}
