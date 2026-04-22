package com.example.artistico_android.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.R
import com.example.artistico_android.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginClicked(email: String, password: String, termsAgreed: Boolean) {
        val emailTrimmed = email.trim()

        val emailError = when {
            emailTrimmed.isEmpty() -> R.string.error_email_required
            !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches() -> R.string.error_email_invalid
            else -> null
        }
        val passwordError = when {
            password.isEmpty() -> R.string.error_password_required
            else -> null
        }
        val formError = if (!termsAgreed && emailError == null && passwordError == null) {
            R.string.error_terms_required
        } else null

        _uiState.update { it.copy(emailError = emailError, passwordError = passwordError, formError = formError) }

        if (emailError != null || passwordError != null || formError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, formError = null) }
            val ok = authRepository.login(emailTrimmed, password)
            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    loginSucceeded = ok,
                    formError = if (!ok) R.string.error_invalid_credentials else null
                )
            }
        }
    }

    /** Called by the Fragment after it has navigated away, so re-entering the screen starts fresh. */
    fun onNavigationConsumed() {
        _uiState.update { it.copy(loginSucceeded = false) }
    }

    /** Clears field-level errors as soon as the user starts typing again. */
    fun onEmailChanged() {
        if (_uiState.value.emailError != null || _uiState.value.formError != null) {
            _uiState.update { it.copy(emailError = null, formError = null) }
        }
    }

    fun onPasswordChanged() {
        if (_uiState.value.passwordError != null || _uiState.value.formError != null) {
            _uiState.update { it.copy(passwordError = null, formError = null) }
        }
    }
}
