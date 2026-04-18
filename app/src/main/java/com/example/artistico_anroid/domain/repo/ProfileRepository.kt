package com.example.artistico_anroid.domain.repo

import com.example.artistico_anroid.domain.model.Post
import com.example.artistico_anroid.domain.model.ProfileTab
import com.example.artistico_anroid.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeCurrentUser(): Flow<User>
    fun observePosts(userId: String, tab: ProfileTab): Flow<List<Post>>
}
