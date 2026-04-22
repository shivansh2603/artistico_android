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
class SignUpViewModel @Inject constructor(
    @Suppress("unused") private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onSignUpClicked(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        termsAgreed: Boolean
    ) {
        val nameTrimmed = name.trim()
        val emailTrimmed = email.trim()

        val nameError = if (nameTrimmed.isEmpty()) R.string.error_name_required else null
        val emailError = when {
            emailTrimmed.isEmpty() -> R.string.error_email_required
            !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches() -> R.string.error_email_invalid
            else -> null
        }
        val passwordError = when {
            password.isEmpty() -> R.string.error_password_required
            password.length < 4 -> R.string.error_password_short
            else -> null
        }
        val confirmError = when {
            confirmPassword.isEmpty() -> R.string.error_password_required
            confirmPassword != password -> R.string.error_password_mismatch
            else -> null
        }
        val formError = if (
            nameError == null && emailError == null && passwordError == null &&
            confirmError == null && !termsAgreed
        ) R.string.error_terms_required else null

        _uiState.update {
            it.copy(
                nameError = nameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmError,
                formError = formError
            )
        }

        if (nameError != null || emailError != null || passwordError != null ||
            confirmError != null || formError != null
        ) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, formError = null) }
            // Always returns false for now — we just flip the preview flag so the UI can show a toast.
            authRepository.signUp(nameTrimmed, emailTrimmed, password)
            _uiState.update { it.copy(isSubmitting = false, signUpPreviewShown = true) }
        }
    }

    fun onNameChanged() = clearField { it.copy(nameError = null, formError = null) }
    fun onEmailChanged() = clearField { it.copy(emailError = null, formError = null) }
    fun onPasswordChanged() = clearField { it.copy(passwordError = null, formError = null) }
    fun onConfirmPasswordChanged() = clearField { it.copy(confirmPasswordError = null, formError = null) }

    fun onPreviewConsumed() {
        _uiState.update { it.copy(signUpPreviewShown = false) }
    }

    private inline fun clearField(transform: (SignUpUiState) -> SignUpUiState) {
        _uiState.update(transform)
    }
}
