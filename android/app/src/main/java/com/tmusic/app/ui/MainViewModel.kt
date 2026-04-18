package com.tmusic.app.ui

import androidx.lifecycle.ViewModel
import com.tmusic.app.data.TMusicRepository
import com.tmusic.app.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AudioQuality {
    HIGH,
    LOW,
}

enum class PlayerMode {
    MINI,
    FULL,
}

data class UiState(
    val searchQuery: String = "",
    val selectedMood: String = "focus",
    val tracks: List<Track> = emptyList(),
    val localTracks: List<Track> = emptyList(),
    val waveTracks: List<Track> = emptyList(),
    val nowPlaying: Track? = null,
    val favorites: Set<Int> = emptySet(),
    val audioQuality: AudioQuality = AudioQuality.HIGH,
    val playerMode: PlayerMode = PlayerMode.FULL,
    val serverConnected: Boolean = true,
)

class MainViewModel : ViewModel() {
    private val repository = TMusicRepository()
    private val _state = MutableStateFlow(UiState(tracks = repository.getTracks()))
    val state = _state.asStateFlow()

    fun search(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                tracks = repository.searchTracksOrAlbums(query),
            )
        }
    }

    fun setLocalTracks(tracks: List<Track>) {
        _state.update { it.copy(localTracks = tracks) }
    }

    fun toggleFavorite(trackId: Int) {
        repository.toggleFavorite(trackId)
        _state.update {
            it.copy(
                favorites = repository.getTracks().map { track -> track.id }.filter(repository::isFavorite).toSet(),
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

    fun setAudioQuality(quality: AudioQuality) {
        _state.update { it.copy(audioQuality = quality) }
    }

    fun setPlayerMode(mode: PlayerMode) {
        _state.update { it.copy(playerMode = mode) }
    }

    fun play(track: Track) {
        _state.update { it.copy(nowPlaying = track) }
    }
}
