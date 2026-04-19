package com.example.artistico_android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistico_android.domain.model.ProfileTab
import com.example.artistico_android.domain.repo.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val activeTab = MutableStateFlow(ProfileTab.POSTS)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val userFlow = profileRepository.observeCurrentUser()
        val postsFlow = userFlow.flatMapLatest { user ->
            activeTab.flatMapLatest { tab -> profileRepository.observePosts(user.id, tab) }
        }

        combine(userFlow, activeTab, postsFlow) { user, tab, posts ->
            ProfileUiState(isLoading = false, user = user, activeTab = tab, posts = posts)
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun onTabSelected(tab: ProfileTab) {
        activeTab.value = tab
    }
}
