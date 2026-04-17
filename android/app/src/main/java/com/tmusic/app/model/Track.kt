package com.tmusic.app.model

data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val genre: String,
    val mood: String,
    val energy: Double,
    val bpm: Int
)

data class WaveResponse(
    val user_id: String,
    val mood: String,
    val recommendations: List<Track>
)
