package com.faruk.musicplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faruk.musicplayer.MusicPlayer
import kotlinx.coroutines.delay

@Composable
fun MusicPlayerScreen() {
    val musicPlayer = remember { MusicPlayer() }
    var currentSong by remember { mutableStateOf("Şarkı seçilmedi") }
    var artist by remember { mutableStateOf("Sanatçı") }
    var songList by remember { 
        mutableStateOf(
            listOf(
                "Şarkı 1" to "Sanatçı 1",
                "Şarkı 2" to "Sanatçı 2",
                "Şarkı 3" to "Sanatçı 3"
            )
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            musicPlayer.updateCurrentPosition()
            delay(100)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Text(
            "🎵 Faruk Music Player",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1DB954),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Album Art Placeholder
        Surface(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF282828)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MusicNote,
                    contentDescription = "Album Art",
                    modifier = Modifier.size(100.dp),
                    tint = Color(0xFF1DB954)
                )
            }
        }

        // Song Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                currentSong,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                artist,
                fontSize = 16.sp,
                color = Color(0xFFB3B3B3),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Progress Bar
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
        ) {
            Slider(
                value = musicPlayer.currentPosition.value.toFloat(),
                onValueChange = { musicPlayer.seekTo(it.toInt()) },
                valueRange = 0f..musicPlayer.duration.value.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF1DB954),
                    activeTrackColor = Color(0xFF1DB954)
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    formatTime(musicPlayer.currentPosition.value),
                    color = Color(0xFFB3B3B3),
                    fontSize = 12.sp
                )
                Text(
                    formatTime(musicPlayer.duration.value),
                    color = Color(0xFFB3B3B3),
                    fontSize = 12.sp
                )
            }
        }

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Shuffle */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = Color(0xFFB3B3B3),
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = { /* Previous */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    if (musicPlayer.isPlaying.value) {
                        musicPlayer.pause()
                    } else {
                        musicPlayer.resume()
                    }
                },
                containerColor = Color(0xFF1DB954),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    if (musicPlayer.isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = { /* Next */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = { /* Repeat */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Repeat,
                    contentDescription = "Repeat",
                    tint = Color(0xFFB3B3B3),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Volume Control
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                "Ses Seviyesi",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = musicPlayer.volume.value,
                onValueChange = { musicPlayer.setVolume(it) },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF1DB954),
                    activeTrackColor = Color(0xFF1DB954)
                )
            )
        }

        // Song List
        Text(
            "Şarkı Listesi",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 24.dp, bottom = 12.dp)
        )

        songList.forEach { (song, art) ->
            SongListItem(
                songName = song,
                artistName = art,
                onClick = {
                    currentSong = song
                    artist = art
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SongListItem(
    songName: String,
    artistName: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = Color(0xFF282828),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.MusicNote,
                contentDescription = "Music",
                tint = Color(0xFF1DB954),
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    songName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    artistName,
                    color = Color(0xFFB3B3B3),
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color(0xFF1DB954)
                )
            }
        }
    }
}

fun formatTime(ms: Int): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / 1000) / 60
    return String.format("%02d:%02d", minutes, seconds)
}