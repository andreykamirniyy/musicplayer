package com.tmusic.app.ui

import androidx.lifecycle.ViewModel
import com.tmusic.app.data.TMusicRepository
import com.tmusic.app.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UiState(
    val selectedMood: String = "focus",
    val tracks: List<Track> = emptyList(),
    val waveTracks: List<Track> = emptyList(),
    val nowPlaying: Track? = null,
    val favorites: Set<Int> = emptySet(),
)

class MainViewModel : ViewModel() {
    private val repository = TMusicRepository()
    private val _state = MutableStateFlow(UiState(tracks = repository.getTracks()))
    val state = _state.asStateFlow()

    fun toggleFavorite(trackId: Int) {
        repository.toggleFavorite(trackId)
        _state.update {
            it.copy(
                favorites = it.tracks.map { track -> track.id }.filter(repository::isFavorite).toSet(),
                waveTracks = repository.generateWave(it.selectedMood),
            )
        }
    }

    fun selectMood(mood: String) {
        _state.update {
            it.copy(
                selectedMood = mood,
                waveTracks = repository.generateWave(mood),
            )
        }
    }

    fun play(track: Track) {
        _state.update { it.copy(nowPlaying = track) }
    }
}
