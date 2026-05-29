package com.infrastructure.db

import com.db.UsersTable
import com.domain.model.User
import com.domain.repository.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class UserRepositoryImpl : UserRepository {

    override fun createOrGet(firebaseUid: String, email: String?, displayName: String?): User =
        transaction {
            val existing = UsersTable
                .select { UsersTable.firebaseUid eq firebaseUid }
                .singleOrNull()

            if (existing != null) {
                return@transaction User(
                    id = existing[UsersTable.id],
                    firebaseUid = existing[UsersTable.firebaseUid],
                    email = existing[UsersTable.email],
                    displayName = existing[UsersTable.displayName]
                )
            }

            val newId = UUID.randomUUID()
            UsersTable.insert {
                it[id] = newId
                it[UsersTable.firebaseUid] = firebaseUid
                it[UsersTable.email] = email
                it[UsersTable.displayName] = displayName
                it[createdAt] = Instant.now()
            }

            User(
                id = newId,
                firebaseUid = firebaseUid,
                email = email,
                displayName = displayName
            )
        }

    override fun findById(userId: UUID): User? =
        transaction {
            UsersTable
                .select { UsersTable.id eq userId }
                .singleOrNull()
                ?.let { row ->
                    User(
                        id = row[UsersTable.id],
                        firebaseUid = row[UsersTable.firebaseUid],
                        email = row[UsersTable.email],
                        displayName = row[UsersTable.displayName]
                    )
                }
        }

    override fun countAll(): Long =
        transaction {
            UsersTable.selectAll().count()
        }
}
