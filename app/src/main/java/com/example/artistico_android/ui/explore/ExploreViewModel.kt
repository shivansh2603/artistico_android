package com.example.artistico_android.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.domain.repo.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState(isLoading = true))
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        observeSections()
    }

    private fun observeSections() {
        viewModelScope.launch {
            postRepository.observeExploreSections()
                .onEach { sections ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sections = sections,
                        error = null
                    )
                }
                .catch { /* surface error via UiText when we wire real backend */ }
                .collect {}
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            postRepository.refresh()
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    fun onLikeClicked(postId: String) {
        viewModelScope.launch { postRepository.toggleLike(postId) }
    }
}
