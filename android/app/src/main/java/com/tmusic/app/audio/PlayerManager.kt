package com.tmusic.app.audio

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tmusic.app.model.Track
import com.tmusic.app.ui.AudioQuality

class PlayerManager(private val context: Context) {

    fun play(track: Track, quality: AudioQuality) {
        val url = when (quality) {
            AudioQuality.HIGH -> track.streamUrlHigh
            AudioQuality.LOW -> track.streamUrlLow
        }

        val intent = Intent(context, ForegroundPlaybackService::class.java).apply {
            action = ForegroundPlaybackService.ACTION_PLAY
            putExtra(ForegroundPlaybackService.EXTRA_URL, url)
            putExtra(ForegroundPlaybackService.EXTRA_TITLE, track.title)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop() {
        val intent = Intent(context, ForegroundPlaybackService::class.java).apply {
            action = ForegroundPlaybackService.ACTION_STOP
        }
        context.startService(intent)
    }

    fun release() {
        // no-op: lifecycle is handled by foreground service
    }
}
