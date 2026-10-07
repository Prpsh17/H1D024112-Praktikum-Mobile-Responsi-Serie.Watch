package com.pemmob.responsi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.responsi.data.model.AnimeDto
import com.pemmob.responsi.data.repository.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class GenreFilterItem(
    val id: Int,
    val name: String
)

class AnimeViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()

    private val _animeListState = MutableStateFlow<UiState<List<AnimeDto>>>(UiState.Loading)
    val animeListState: StateFlow<UiState<List<AnimeDto>>> = _animeListState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<AnimeDto>>(UiState.Idle)
    val detailState: StateFlow<UiState<AnimeDto>> = _detailState.asStateFlow()

    val availableGenres = listOf(
        GenreFilterItem(1, "Action"),
        GenreFilterItem(2, "Adventure"),
        GenreFilterItem(4, "Comedy"),
        GenreFilterItem(8, "Drama"),
        GenreFilterItem(10, "Fantasy"),
        GenreFilterItem(22, "Romance"),
        GenreFilterItem(24, "Sci-Fi"),
        GenreFilterItem(36, "Slice of Life"),
        GenreFilterItem(37, "Supernatural")
    )

    private var searchJob: Job? = null

    init {
        fetchAnime()
    }

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            fetchAnime()
        }
    }

    fun onGenreSelected(genreId: Int?) {
        _selectedGenreId.value = if (_selectedGenreId.value == genreId) null else genreId
        fetchAnime()
    }

    fun fetchAnime() {
        searchJob?.cancel()
        viewModelScope.launch {
            _animeListState.value = UiState.Loading
            try {
                val query = _searchQuery.value
                val genreId = _selectedGenreId.value
                val results = repository.searchAnime(query = query, genreId = genreId)
                _animeListState.value = UiState.Success(results)
            } catch (e: Exception) {
                val message = e.localizedMessage?.takeIf { it.isNotBlank() }
                    ?: "Gagal memuat data anime. Pastikan koneksi internet aktif."
                _animeListState.value = UiState.Error(message)
            }
        }
    }

    fun fetchAnimeDetail(malId: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val detail = repository.getAnimeDetail(malId)
                _detailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                val message = e.localizedMessage?.takeIf { it.isNotBlank() }
                    ?: "Gagal memuat detail anime."
                _detailState.value = UiState.Error(message)
            }
        }
    }
}