package com.example.artistico_android.domain.repo

import com.example.artistico_android.domain.model.ChatPreview
import com.example.artistico_android.domain.model.Community
import kotlinx.coroutines.flow.Flow

interface ConnectRepository {
    fun observeCommunities(query: String = ""): Flow<List<Community>>
    fun observeChats(query: String = ""): Flow<List<ChatPreview>>
}
