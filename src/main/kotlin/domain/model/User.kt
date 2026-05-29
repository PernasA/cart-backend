package com.domain.model

import java.util.UUID

data class User(
    val id: UUID,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?
)
