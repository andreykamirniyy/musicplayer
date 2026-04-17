package com.tmusic.app.data

import com.tmusic.app.model.Track

class TMusicRepository {
    private val demoTracks = listOf(
        Track(1, "Night Drive", "Polar Lights", "synthwave", "focus", 0.62, 114),
        Track(2, "Sunrise Pulse", "Neon District", "pop", "happy", 0.78, 126),
        Track(3, "Soft Rain", "Cloud Harbor", "lofi", "calm", 0.32, 84),
        Track(4, "City Run", "Nova Run", "electronic", "energetic", 0.88, 132),
        Track(5, "Deep Letters", "Velvet Lane", "indie", "sad", 0.40, 92),
    )

    private val favorites = mutableSetOf<Int>()

    fun getTracks(): List<Track> = demoTracks

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
