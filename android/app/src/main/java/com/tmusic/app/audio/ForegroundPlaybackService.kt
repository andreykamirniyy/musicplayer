package com.tmusic.app.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class ForegroundPlaybackService : Service() {
    private var player: ExoPlayer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        player = ExoPlayer.Builder(this).build()
        startForeground(NOTIFICATION_ID, notification("Подготовка плеера"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_URL) ?: return START_STICKY
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Т-Музыка"
                player?.setMediaItem(MediaItem.fromUri(url))
                player?.prepare()
                player?.playWhenReady = true
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(NOTIFICATION_ID, notification("Сейчас играет: $title"))
            }

            ACTION_STOP -> {
                player?.stop()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "T-Music Playback",
                NotificationManager.IMPORTANCE_LOW,
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun notification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Т-Музыка")
            .setContentText(content)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val ACTION_PLAY = "com.tmusic.app.PLAY"
        const val ACTION_STOP = "com.tmusic.app.STOP"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_TITLE = "extra_title"

        private const val CHANNEL_ID = "tmusic_playback"
        private const val NOTIFICATION_ID = 1001
    }
}
