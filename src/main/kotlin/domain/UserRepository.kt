package com.domain

import com.db.UsersTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object UserRepository {

    fun createOrGetUser(
        firebaseUid: String,
        email: String?,
        displayName: String?
    ): UUID =
        transaction {

            // Look if the user already exists
            val existingUserId = UsersTable
                .select { UsersTable.firebaseUid eq firebaseUid }
                .map { it[UsersTable.id] }
                .singleOrNull()

            if (existingUserId != null) {
                return@transaction existingUserId
            }

            // Create new user
            UsersTable.insert {
                it[id] = UUID.randomUUID()
                it[UsersTable.firebaseUid] = firebaseUid
                it[UsersTable.email] = email
                it[UsersTable.displayName] = displayName
                it[createdAt] = Instant.now()
            }[UsersTable.id]
        }

    fun exists(userId: UUID): Boolean =
        transaction {
            UsersTable
                .select { UsersTable.id eq userId }
                .count() > 0
        }
}
