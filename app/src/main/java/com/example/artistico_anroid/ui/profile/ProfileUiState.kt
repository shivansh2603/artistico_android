package com.example.artistico_anroid.ui.profile

import com.example.artistico_anroid.domain.model.Post
import com.example.artistico_anroid.domain.model.ProfileTab
import com.example.artistico_anroid.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val activeTab: ProfileTab = ProfileTab.POSTS,
    val posts: List<Post> = emptyList()
)
