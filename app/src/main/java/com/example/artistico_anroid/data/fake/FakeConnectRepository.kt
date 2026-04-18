package com.example.artistico_anroid.data.fake

import com.example.artistico_anroid.domain.model.ChatPreview
import com.example.artistico_anroid.domain.model.Community
import com.example.artistico_anroid.domain.repo.ConnectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The screenshot shows an empty state, so we default to empty lists.
 * Seed data is wired behind a simple toggle to make it easy to see the populated UI during dev.
 */
@Singleton
class FakeConnectRepository @Inject constructor() : ConnectRepository {

    private val communities = MutableStateFlow<List<Community>>(emptyList())
    private val chats = MutableStateFlow<List<ChatPreview>>(emptyList())

    override fun observeCommunities(query: String): Flow<List<Community>> =
        communities.map { list ->
            if (query.isBlank()) list else list.filter {
                it.name.contains(query, ignoreCase = true) || it.tag.contains(query, ignoreCase = true)
            }
        }

    override fun observeChats(query: String): Flow<List<ChatPreview>> =
        chats.map { list ->
            if (query.isBlank()) list else list.filter { it.title.contains(query, ignoreCase = true) }
        }
}
