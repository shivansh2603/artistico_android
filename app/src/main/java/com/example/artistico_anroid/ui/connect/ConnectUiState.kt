package com.example.artistico_anroid.ui.connect

import com.example.artistico_anroid.domain.model.ChatPreview
import com.example.artistico_anroid.domain.model.Community
import com.example.artistico_anroid.domain.model.ConnectTab

data class ConnectUiState(
    val activeTab: ConnectTab = ConnectTab.COMMUNITIES,
    val query: String = "",
    val communities: List<Community> = emptyList(),
    val chats: List<ChatPreview> = emptyList()
) {
    val isEmpty: Boolean
        get() = when (activeTab) {
            ConnectTab.COMMUNITIES -> communities.isEmpty()
            ConnectTab.CHATS -> chats.isEmpty()
        }
}
