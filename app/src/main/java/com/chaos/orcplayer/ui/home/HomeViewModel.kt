package com.chaos.orcplayer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.orcplayer.domain.model.MediaItem
import com.chaos.orcplayer.domain.usecase.ScanMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Filter modes for the media grid. */
sealed interface HomeFilter {
    object All : HomeFilter
    object Audio : HomeFilter
    object Video : HomeFilter
    object Recent : HomeFilter
}

/** Current state of the home screen view model. */
data class HomeState(
    val items: List<MediaItem> = emptyList(),
    val filteredItems: List<MediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: HomeFilter = HomeFilter.All,
    val searchQuery: String = "",
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scanMediaUseCase: ScanMediaUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadMedia()
    }

    /** Fetch media items from MediaStore. */
    fun loadMedia() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val items = scanMediaUseCase.execute()
                _state.value = HomeState(
                    items = items,
                    filteredItems = filterItems(items, HomeFilter.All, ""),
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.message ?: "Scan failed")
                }
            }
        }
    }

    /** Change the current filter mode. */
    fun setFilter(filter: HomeFilter) {
        val prev = _state.value
        _state.value = prev.copy(
            filter = filter,
            filteredItems = filterItems(prev.items, filter, prev.searchQuery),
        )
    }

    /** Update the search query text. */
    fun setSearchQuery(query: String) {
        val prev = _state.value
        _state.value = prev.copy(
            searchQuery = query,
            filteredItems = filterItems(prev.items, prev.filter, query),
        )
    }

    /** Apply filter + search to the full item list. */
    private fun filterItems(items: List<MediaItem>, filter: HomeFilter, query: String): List<MediaItem> {
        var result = when (filter) {
            is HomeFilter.Audio -> items.filter { it.type == MediaItem.MediaType.Audio }
            is HomeFilter.Video -> items.filter { it.type == MediaItem.MediaType.Video }
            is HomeFilter.Recent -> items.sortedByDescending { it.lastModified }.take(50)
            else -> items
        }
        if (query.isNotBlank()) {
            val q = query.lowercase()
            result = result.filter {
                it.title.lowercase().contains(q) ||
                it.artist?.lowercase()?.contains(q) == true
            }
        }
        return result
    }

    /** Helper to update MutableStateFlow value immutably. */
    private fun <T> MutableStateFlow<T>.update(block: (T) -> T) {
        val current = value
        value = block(current)
    }
}
