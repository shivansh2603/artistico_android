package com.example.artistico_anroid.domain.repo

import com.example.artistico_anroid.domain.model.ChatPreview
import com.example.artistico_anroid.domain.model.Community
import kotlinx.coroutines.flow.Flow

interface ConnectRepository {
    fun observeCommunities(query: String = ""): Flow<List<Community>>
    fun observeChats(query: String = ""): Flow<List<ChatPreview>>
}
