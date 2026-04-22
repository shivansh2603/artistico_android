package com.example.artistico_android.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.domain.repo.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun markOnboardingComplete() {
        viewModelScope.launch {
            onboardingRepository.markComplete()
            _uiState.update { it.copy(onboardingCompleted = true) }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(onboardingCompleted = false) }
    }
}
