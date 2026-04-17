package com.tmusic.app.data

import com.tmusic.app.model.Track

class TMusicRepository {
    private val demoTracks = listOf(
        Track(
            1,
            "Night Drive",
            "Polar Lights",
            "City Lights",
            "synthwave",
            "focus",
            0.62,
            114,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3?quality=low",
        ),
        Track(
            2,
            "Sunrise Pulse",
            "Neon District",
            "Morning Neon",
            "pop",
            "happy",
            0.78,
            126,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3?quality=low",
        ),
        Track(
            3,
            "Soft Rain",
            "Cloud Harbor",
            "Window Mood",
            "lofi",
            "calm",
            0.32,
            84,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3?quality=low",
        ),
        Track(
            4,
            "City Run",
            "Nova Run",
            "Street Motion",
            "electronic",
            "energetic",
            0.88,
            132,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3?quality=low",
        ),
        Track(
            5,
            "Deep Letters",
            "Velvet Lane",
            "Paper Hearts",
            "indie",
            "sad",
            0.40,
            92,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3?quality=low",
        ),
    )

    private val favorites = mutableSetOf<Int>()

    fun getTracks(): List<Track> = demoTracks

    fun searchTracksOrAlbums(query: String): List<Track> {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) return demoTracks
        return demoTracks.filter {
            it.title.lowercase().contains(normalized) ||
                it.album.lowercase().contains(normalized) ||
                it.artist.lowercase().contains(normalized)
        }
    }

    fun toggleFavorite(trackId: Int) {
        if (!favorites.add(trackId)) {
            favorites.remove(trackId)
        }
    }

    fun isFavorite(trackId: Int): Boolean = favorites.contains(trackId)

    fun generateWave(mood: String): List<Track> {
        val favTracks = demoTracks.filter { favorites.contains(it.id) }
        if (favTracks.isEmpty()) {
            return demoTracks.filter { it.mood == mood }.ifEmpty { demoTracks }
        }

        val targetGenre = favTracks.groupingBy { it.genre }.eachCount().maxBy { it.value }.key
        return demoTracks
            .filterNot { favorites.contains(it.id) }
            .sortedByDescending { track ->
                val similarity = (if (track.genre == targetGenre) 1.0 else 0.6) +
                    (1.0 - kotlin.math.abs(track.energy - favTracks.map { it.energy }.average()))
                val moodScore = if (track.mood == mood) 1.0 else 0.2
                0.55 * similarity + 0.30 * moodScore + 0.15 * 0.5
            }
    }
}
