package com.example.artistico_android.ui.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.domain.repo.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {

    private val postId: String = requireNotNull(savedStateHandle["postId"]) {
        "postId argument is required for PostDetailsFragment"
    }

    private val _uiState = MutableStateFlow(PostDetailsUiState())
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                postRepository.observePost(postId),
                postRepository.observeComments(postId)
            ) { post, comments -> post to comments }
                .onEach { (post, comments) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        post = post,
                        comments = comments
                    )
                }
                .collect {}
        }
    }

    fun onLikeClicked() {
        viewModelScope.launch { postRepository.toggleLike(postId) }
    }

    fun onSendComment(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmittingComment = true)
            postRepository.addComment(postId, text)
            _uiState.value = _uiState.value.copy(isSubmittingComment = false)
        }
    }
}
