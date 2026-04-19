package com.example.artistico_android.domain.model

enum class UserRole { DEV, USER }

data class User(
    val id: String,
    val displayName: String,
    val bio: String? = null,
    val role: UserRole = UserRole.USER,
    val avatarRes: Int? = null,
    val avatarUrl: String? = null,
    val isPremium: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0
)
