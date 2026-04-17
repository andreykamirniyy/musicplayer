package com.tmusic.app.model

data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val mood: String,
    val energy: Double,
    val bpm: Int,
    val streamUrlHigh: String,
    val streamUrlLow: String,
)

data class WaveResponse(
    val user_id: String,
    val mood: String,
    val recommendations: List<Track>
)
