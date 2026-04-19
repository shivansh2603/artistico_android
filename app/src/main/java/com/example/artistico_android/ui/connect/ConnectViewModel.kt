package com.example.artistico_android.ui.connect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.domain.model.ConnectTab
import com.example.artistico_android.domain.repo.ConnectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository
) : ViewModel() {

    private val activeTab = MutableStateFlow(ConnectTab.COMMUNITIES)
    private val query = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ConnectUiState())
    val uiState: StateFlow<ConnectUiState> = _uiState.asStateFlow()

    init {
        val debouncedQuery = query.debounce(200).distinctUntilChanged()

        val communitiesFlow = debouncedQuery
            .flatMapLatest { connectRepository.observeCommunities(it) }
        val chatsFlow = debouncedQuery
            .flatMapLatest { connectRepository.observeChats(it) }

        combine(activeTab, query, communitiesFlow, chatsFlow) { tab, q, communities, chats ->
            ConnectUiState(tab, q, communities, chats)
        }.onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(q: String) {
        query.value = q
    }

    fun onTabSelected(tab: ConnectTab) {
        activeTab.value = tab
        // Refresh current snapshot so tab toggle reflects immediately even before flows re-emit.
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }
}
