package com.tmusic.app.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.tmusic.app.model.Track

object LocalAudioLoader {
    fun load(context: Context): List<Track> {
        val tracks = mutableListOf<Track>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
        )

        val sort = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            sort,
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            var idx = 100000
            while (cursor.moveToNext()) {
                val mediaId = cursor.getLong(idCol)
                val title = cursor.getString(titleCol) ?: "Unknown"
                val artist = cursor.getString(artistCol) ?: "Unknown"
                val album = cursor.getString(albumCol) ?: "Unknown"
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaId).toString()
                tracks.add(
                    Track(
                        id = idx++,
                        title = title,
                        artist = artist,
                        album = album,
                        genre = "local",
                        mood = "calm",
                        energy = 0.5,
                        bpm = 100,
                        streamUrlHigh = uri,
                        streamUrlLow = uri,
                    ),
                )
            }
        }
        return tracks
    }
}
