package com.example.artistico_anroid.ui.post

import com.example.artistico_anroid.domain.model.Comment
import com.example.artistico_anroid.domain.model.Post

data class PostDetailsUiState(
    val isLoading: Boolean = true,
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isSubmittingComment: Boolean = false
) {
    val showEmptyComments: Boolean get() = !isLoading && comments.isEmpty()
}
