package com.noisefit.smartlearning.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.noisefit.smartlearning.data.AuthRepository

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, errorMessage = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, errorMessage = null)
    }

    suspend fun login(): Boolean {
        val validationError = validateCredentials()
        if (validationError != null) {
            uiState = uiState.copy(errorMessage = validationError)
            return false
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        val result = authRepository.login(
            email = uiState.email.trim(),
            password = uiState.password
        )

        uiState = uiState.copy(
            isLoading = false,
            errorMessage = result.exceptionOrNull()?.message
        )

        return result.isSuccess
    }

    private fun validateCredentials(): String? {
        val email = uiState.email.trim()
        val password = uiState.password

        return when {
            email.isBlank() -> "Email is required."
            !EMAIL_REGEX.matches(email) -> "Enter a valid email address."
            password.isBlank() -> "Password is required."
            password.length < MIN_PASSWORD_LENGTH -> "Password must be at least 6 characters."
            else -> null
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
