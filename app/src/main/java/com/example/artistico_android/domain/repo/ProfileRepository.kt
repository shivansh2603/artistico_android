package com.example.artistico_android.domain.repo

import com.example.artistico_android.domain.model.Post
import com.example.artistico_android.domain.model.ProfileTab
import com.example.artistico_android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeCurrentUser(): Flow<User>
    fun observePosts(userId: String, tab: ProfileTab): Flow<List<Post>>
}
