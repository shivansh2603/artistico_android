package com.example.artistico_anroid.ui.explore

import com.example.artistico_anroid.core.common.UiText
import com.example.artistico_anroid.domain.model.ExploreSection

data class ExploreUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val sections: List<ExploreSection> = emptyList(),
    val error: UiText? = null
) {
    val isEmpty: Boolean get() = !isLoading && sections.isEmpty() && error == null
}
