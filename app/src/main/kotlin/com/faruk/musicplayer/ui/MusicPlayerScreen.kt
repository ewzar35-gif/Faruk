package com.faruk.musicplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faruk.musicplayer.MusicLibrary
import com.faruk.musicplayer.MusicPlayer
import com.faruk.musicplayer.Song
import com.faruk.musicplayer.ui.theme.LanguageManager
import kotlinx.coroutines.launch

@Composable
fun MusicPlayerScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val musicPlayer = remember { MusicPlayer() }
    val musicLibrary = remember { MusicLibrary(context) }
    
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var songList by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoadingSongs by remember { mutableStateOf(true) }
    var showPlaylist by remember { mutableStateOf(false) }
    
    val language = LanguageManager.currentLanguage.value

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                songList = musicLibrary.getAllSongs()
                isLoadingSongs = false
            } catch (e: Exception) {
                e.printStackTrace()
                isLoadingSongs = false
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            musicPlayer.updateCurrentPosition()
            kotlinx.coroutines.delay(100)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header with Language Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                LanguageManager.getString("title"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1DB954)
            )
            
            IconButton(
                onClick = {
                    LanguageManager.toggleLanguage()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Language,
                    contentDescription = "Toggle Language",
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

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
                currentSong?.title ?: LanguageManager.getString("no_song_selected"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            Text(
                currentSong?.artist ?: LanguageManager.getString("artist"),
                fontSize = 16.sp,
                color = Color(0xFFB3B3B3),
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1
            )
        }

        // Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Slider(
                value = musicPlayer.currentPosition.value.toFloat(),
                onValueChange = { musicPlayer.seekTo(it.toInt()) },
                valueRange = 0f..maxOf(musicPlayer.duration.value.toFloat(), 1f),
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
                    contentDescription = LanguageManager.getString("shuffle"),
                    tint = Color(0xFFB3B3B3),
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    val currentIndex = songList.indexOf(currentSong)
                    if (currentIndex > 0) {
                        val previousSong = songList[currentIndex - 1]
                        currentSong = previousSong
                        musicPlayer.play(previousSong.data)
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = LanguageManager.getString("previous"),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    if (currentSong != null) {
                        if (musicPlayer.isPlaying.value) {
                            musicPlayer.pause()
                        } else {
                            musicPlayer.resume()
                        }
                    }
                },
                containerColor = Color(0xFF1DB954),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    if (musicPlayer.isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = LanguageManager.getString("play_pause"),
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = {
                    val currentIndex = songList.indexOf(currentSong)
                    if (currentIndex < songList.size - 1) {
                        val nextSong = songList[currentIndex + 1]
                        currentSong = nextSong
                        musicPlayer.play(nextSong.data)
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = LanguageManager.getString("next"),
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
                    contentDescription = LanguageManager.getString("repeat"),
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
                LanguageManager.getString("volume_label"),
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

        // Playlist Toggle Button
        Button(
            onClick = { showPlaylist = !showPlaylist },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
        ) {
            Icon(
                Icons.Default.PlaylistPlay,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                if (showPlaylist) "Çal.Gizle" else LanguageManager.getString("playlist"),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // Playlist Section
        if (showPlaylist) {
            if (isLoadingSongs) {
                Text(
                    LanguageManager.getString("loading_songs"),
                    color = Color(0xFFB3B3B3),
                    modifier = Modifier.padding(16.dp)
                )
            } else if (songList.isEmpty()) {
                Text(
                    LanguageManager.getString("no_songs"),
                    color = Color(0xFFB3B3B3),
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        "${songList.size} ${LanguageManager.getString("songs_found")}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(songList) { song ->
                            SongListItem(
                                song = song,
                                isSelected = currentSong?.id == song.id,
                                onClick = {
                                    currentSong = song
                                    musicPlayer.play(song.data)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongListItem(
    song: Song,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        color = if (isSelected) Color(0xFF1DB954).copy(alpha = 0.2f) else Color(0xFF282828),
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
                    song.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    song.artist,
                    color = Color(0xFFB3B3B3),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Text(
                formatTime(song.duration.toInt()),
                color = Color(0xFFB3B3B3),
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            if (isSelected) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Playing",
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(20.dp)
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