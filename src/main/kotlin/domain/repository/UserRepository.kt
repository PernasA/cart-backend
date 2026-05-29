package com.domain.repository

import com.domain.model.User
import java.util.UUID

interface UserRepository {
    fun createOrGet(firebaseUid: String, email: String?, displayName: String?): User
    fun findById(userId: UUID): User?
    fun countAll(): Long
}
