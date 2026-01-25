package com.example.newsapp.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUiState(
    val emailOrMobile: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoginSuccessful: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Hardcoded credentials as per BRS
    private val validEmail = "test@example.com"
    private val validMobile = "1234567890"
    private val validPassword = "password123"

    fun onEmailOrMobileChange(value: String) {
        _uiState.update {
            it.copy(
                emailOrMobile = value,
                emailError = null,
                loginError = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                loginError = null
            )
        }
    }

    fun onSignInClick() {
        val currentState = _uiState.value
        var hasError = false

        // Validate email/mobile
        val emailOrMobile = currentState.emailOrMobile.trim()
        if (emailOrMobile.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email or mobile number is required") }
            hasError = true
        } else if (!isValidEmailOrMobile(emailOrMobile)) {
            _uiState.update { it.copy(emailError = "Enter a valid email or 10-digit mobile number") }
            hasError = true
        }

        // Validate password
        val password = currentState.password
        if (password.isEmpty()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }

        if (hasError) return

        // Check credentials against hardcoded values
        _uiState.update { it.copy(isLoading = true) }

        val isValidCredentials = (emailOrMobile == validEmail || emailOrMobile == validMobile)
            && password == validPassword

        if (isValidCredentials) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoginSuccessful = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginError = "Invalid credentials. Please try again.",
                    emailOrMobile = "",
                    password = ""
                )
            }
        }
    }

    fun clearLoginError() {
        _uiState.update { it.copy(loginError = null) }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }

    private fun isValidEmailOrMobile(input: String): Boolean {
        // Check if it's a valid email
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        if (emailPattern.matcher(input).matches()) {
            return true
        }

        // Check if it's a valid 10-digit mobile number
        val mobilePattern = "^[0-9]{10}$".toRegex()
        if (mobilePattern.matches(input)) {
            return true
        }

        return false
    }
}
