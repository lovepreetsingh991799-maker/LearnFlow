package com.noisefit.smartlearning.data

import kotlinx.coroutines.delay

class AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit> {
        delay(LOGIN_DELAY_MS)

        return if (email.equals(MOCK_FAILURE_EMAIL, ignoreCase = true) || password == "fail123") {
            Result.failure(IllegalArgumentException("Mock login failed. Try another valid account."))
        } else {
            Result.success(Unit)
        }
    }

    private companion object {
        const val LOGIN_DELAY_MS = 650L
        const val MOCK_FAILURE_EMAIL = "error@example.com"
    }
}
