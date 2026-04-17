package com.tmusic.app.audio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.tmusic.app.model.Track
import com.tmusic.app.ui.AudioQuality

class PlayerManager(context: Context) {
    private val player = ExoPlayer.Builder(context).build()

    fun play(track: Track, quality: AudioQuality) {
        val url = when (quality) {
            AudioQuality.HIGH -> track.streamUrlHigh
            AudioQuality.LOW -> track.streamUrlLow
        }
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    fun release() {
        player.release()
    }
}
