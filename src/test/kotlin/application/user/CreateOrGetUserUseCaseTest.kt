package com.application.user

import com.domain.model.User
import com.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateOrGetUserUseCaseTest {

    @Test
    fun `execute with a new firebaseUid returns user with the correct data`() {
        val userRepository = mockk<UserRepository>()
        val useCase = CreateOrGetUserUseCase(userRepository)

        val firebaseUid = "new-firebase-uid-abc123"
        val email = "new@example.com"
        val displayName = "New User"
        val expectedUser = User(
            id = UUID.randomUUID(),
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName
        )

        every { userRepository.createOrGet(firebaseUid, email, displayName) } returns expectedUser

        val result = useCase.execute(firebaseUid, email, displayName)

        assertNotNull(result)
        assertEquals(expectedUser, result)
        assertEquals(firebaseUid, result.firebaseUid)
        assertEquals(email, result.email)
        assertEquals(displayName, result.displayName)
    }

    @Test
    fun `execute with an existing firebaseUid returns the existing user (idempotent)`() {
        val userRepository = mockk<UserRepository>()
        val useCase = CreateOrGetUserUseCase(userRepository)

        val existingUserId = UUID.randomUUID()
        val firebaseUid = "existing-firebase-uid-xyz789"
        val email = "existing@example.com"
        val displayName = "Existing User"
        val existingUser = User(
            id = existingUserId,
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName
        )

        every { userRepository.createOrGet(firebaseUid, email, displayName) } returns existingUser

        val firstResult = useCase.execute(firebaseUid, email, displayName)
        val secondResult = useCase.execute(firebaseUid, email, displayName)

        assertEquals(existingUser, firstResult)
        assertEquals(existingUser, secondResult)
        assertEquals(firstResult.id, secondResult.id)
    }

    @Test
    fun `execute calls userRepository createOrGet exactly once with the correct parameters`() {
        val userRepository = mockk<UserRepository>()
        val useCase = CreateOrGetUserUseCase(userRepository)

        val firebaseUid = "uid-verify-call"
        val email = "verify@example.com"
        val displayName = "Verify User"
        val expectedUser = User(
            id = UUID.randomUUID(),
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName
        )

        every { userRepository.createOrGet(firebaseUid, email, displayName) } returns expectedUser

        useCase.execute(firebaseUid, email, displayName)

        verify(exactly = 1) { userRepository.createOrGet(firebaseUid, email, displayName) }
    }
}
