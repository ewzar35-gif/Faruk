package com.faruk.musicplayer

import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import kotlin.math.max
import kotlin.math.min

class MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    
    val isPlaying = mutableStateOf(false)
    val currentPosition = mutableStateOf(0)
    val duration = mutableStateOf(0)
    val volume = mutableStateOf(0.5f)

    fun play(filePath: String) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
                duration.value = duration
                isPlaying.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        try {
            mediaPlayer?.pause()
            isPlaying.value = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resume() {
        try {
            mediaPlayer?.start()
            isPlaying.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPlaying.value = false
            currentPosition.value = 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
            currentPosition.value = position
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setVolume(level: Float) {
        val normalizedVolume = max(0f, min(1f, level))
        volume.value = normalizedVolume
        mediaPlayer?.setVolume(normalizedVolume, normalizedVolume)
    }

    fun updateCurrentPosition() {
        try {
            if (isPlaying.value) {
                currentPosition.value = mediaPlayer?.currentPosition ?: 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}