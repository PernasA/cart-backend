package com.application.user

import com.domain.model.User
import com.domain.repository.UserRepository

class CreateOrGetUserUseCase(private val userRepository: UserRepository) {

    fun execute(firebaseUid: String, email: String?, displayName: String?): User {
        return userRepository.createOrGet(firebaseUid, email, displayName)
    }
}
