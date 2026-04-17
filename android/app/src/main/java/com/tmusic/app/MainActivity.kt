package com.tmusic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tmusic.app.audio.PlayerManager
import com.tmusic.app.model.Track
import com.tmusic.app.ui.AudioQuality
import com.tmusic.app.ui.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerManager = PlayerManager(this)
        setContent {
            MaterialTheme {
                TMusicApp(onPlayTrack = { track, quality -> playerManager.play(track, quality) })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}

@Composable
private fun TMusicApp(
    vm: MainViewModel = viewModel(),
    onPlayTrack: (Track, AudioQuality) -> Unit,
) {
    val state by vm.state.collectAsState()
    val moods = listOf("focus", "happy", "calm", "energetic", "sad")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Т-Музыка MVP", style = MaterialTheme.typography.headlineSmall)
            Text("Т-Волна по избранному и настроению")

            AudioQualitySettings(
                selected = state.audioQuality,
                onSelectQuality = vm::setAudioQuality,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                moods.forEach { mood ->
                    AssistChip(onClick = { vm.selectMood(mood) }, label = { Text(mood) })
                }
            }

            Text("Треки")
            TrackList(
                tracks = state.tracks,
                favorites = state.favorites,
                onPlay = {
                    vm.play(it)
                    onPlayTrack(it, state.audioQuality)
                },
                onFavorite = vm::toggleFavorite,
            )

            Text("Т-Волна (${state.selectedMood})")
            TrackList(
                tracks = state.waveTracks,
                favorites = state.favorites,
                onPlay = {
                    vm.play(it)
                    onPlayTrack(it, state.audioQuality)
                },
                onFavorite = vm::toggleFavorite,
            )

            state.nowPlaying?.let { now ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Сейчас играет")
                        Text("${now.title} — ${now.artist}")
                        Text(
                            if (state.audioQuality == AudioQuality.HIGH) {
                                "Качество: Наилучшее (высокое)"
                            } else {
                                "Качество: Низкое"
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioQualitySettings(
    selected: AudioQuality,
    onSelectQuality: (AudioQuality) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Настройки качества звука")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = { onSelectQuality(AudioQuality.HIGH) },
                    label = { Text("Наилучшее (высокое)") },
                    enabled = selected != AudioQuality.HIGH,
                )
                AssistChip(
                    onClick = { onSelectQuality(AudioQuality.LOW) },
                    label = { Text("Низкое") },
                    enabled = selected != AudioQuality.LOW,
                )
            }
        }
    }
}

@Composable
private fun TrackList(
    tracks: List<Track>,
    favorites: Set<Int>,
    onPlay: (Track) -> Unit,
    onFavorite: (Int) -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        items(tracks, key = { it.id }) { track ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${track.title} — ${track.artist}")
                        Text("${track.genre} · ${track.mood}")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onPlay(track) }) { Text("Play") }
                        Button(onClick = { onFavorite(track.id) }) {
                            Text(if (favorites.contains(track.id)) "★" else "☆")
                        }
                    }
                }
            }
        }
    }
}
