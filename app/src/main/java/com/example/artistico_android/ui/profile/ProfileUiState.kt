package com.example.artistico_android.ui.profile

import com.example.artistico_android.domain.model.Post
import com.example.artistico_android.domain.model.ProfileTab
import com.example.artistico_android.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val activeTab: ProfileTab = ProfileTab.POSTS,
    val posts: List<Post> = emptyList(),
    /** One-shot flag raised after logout() completes so the Fragment can navigate to Login. */
    val loggedOut: Boolean = false
)
