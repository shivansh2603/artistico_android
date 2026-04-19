package com.example.artistico_android.domain.model

import java.time.Instant

data class Post(
    val id: String,
    val author: User,
    val imageRes: Int,
    val imageUrl: String? = null,
    val description: String? = null,
    val hashtags: List<String> = emptyList(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val createdAt: Instant = Instant.now()
)

data class Comment(
    val id: String,
    val postId: String,
    val author: User,
    val text: String,
    val createdAt: Instant = Instant.now()
)

data class ExploreSection(
    val id: String,
    val title: String,
    val posts: List<Post>
)
