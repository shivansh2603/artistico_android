package com.example.artistico_anroid.domain.model

import java.time.Instant

enum class ConnectTab { COMMUNITIES, CHATS }

data class Community(
    val id: String,
    val name: String,
    val tag: String,
    val memberCount: Int,
    val coverRes: Int? = null
)

data class ChatPreview(
    val id: String,
    val title: String,
    val isGroup: Boolean,
    val lastMessage: String?,
    val lastMessageAt: Instant?,
    val unreadCount: Int,
    val avatarRes: Int? = null
)
