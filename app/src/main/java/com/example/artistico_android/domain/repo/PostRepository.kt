package com.example.artistico_android.domain.repo

import com.example.artistico_android.domain.model.Comment
import com.example.artistico_android.domain.model.ExploreSection
import com.example.artistico_android.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun observeExploreSections(): Flow<List<ExploreSection>>
    suspend fun refresh()
    fun observePost(postId: String): Flow<Post?>
    fun observeComments(postId: String): Flow<List<Comment>>
    suspend fun toggleLike(postId: String)
    suspend fun addComment(postId: String, text: String)
}
