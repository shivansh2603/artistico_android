package com.example.artistico_anroid.ui.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_anroid.domain.repo.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        combine(
            challengeRepository.observeActiveChallenge(),
            challengeRepository.observePreviousWeeks()
        ) { active, previous ->
            _uiState.value.copy(
                isLoading = false,
                active = active,
                previousWeeks = previous,
                timeRemainingMs = active?.let { remainingMs(it.endsAt) } ?: 0L
            )
        }.onEach { _uiState.value = it }
            .launchIn(viewModelScope)

        viewModelScope.launch { startTicker() }
    }

    private suspend fun startTicker() {
        while (viewModelScope.isActive) {
            val end = _uiState.value.active?.endsAt
            _uiState.value = _uiState.value.copy(
                timeRemainingMs = end?.let { remainingMs(it) } ?: 0L
            )
            delay(1_000)
        }
    }

    private fun remainingMs(endsAt: Instant): Long =
        (endsAt.toEpochMilli() - Instant.now().toEpochMilli()).coerceAtLeast(0L)
}
