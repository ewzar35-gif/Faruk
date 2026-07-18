package com.faruk.musicplayer.ui.theme

import androidx.compose.runtime.mutableStateOf

enum class Language {
    TURKISH,
    ENGLISH
}

class LanguageManager {
    companion object {
        val currentLanguage = mutableStateOf(Language.TURKISH)
        
        fun getString(stringKey: String): String {
            return when (currentLanguage.value) {
                Language.TURKISH -> StringsTR.getTranslation(stringKey)
                Language.ENGLISH -> StringsEN.getTranslation(stringKey)
            }
        }
        
        fun toggleLanguage() {
            currentLanguage.value = if (currentLanguage.value == Language.TURKISH) {
                Language.ENGLISH
            } else {
                Language.TURKISH
            }
        }
    }
}

object StringsTR {
    private val translations = mapOf(
        "app_name" to "Faruk Müzik Oynatıcı",
        "title" to "🎵 Faruk Müzik Oynatıcı",
        "no_song_selected" to "Şarkı seçilmedi",
        "artist" to "Sanatçı",
        "volume_label" to "Ses Seviyesi",
        "playlist" to "Şarkı Listesi",
        "shuffle" to "Karıştır",
        "previous" to "Önceki",
        "play_pause" to "Oynat/Duraklat",
        "next" to "Sonraki",
        "repeat" to "Tekrarla",
        "no_songs" to "Şarkı bulunamadı",
        "loading_songs" to "Şarkılar yükleniyor...",
        "language" to "Dil: TR/EN",
        "songs_found" to "Şarkı bulundu"
    )
    
    fun getTranslation(key: String): String = translations[key] ?: key
}

object StringsEN {
    private val translations = mapOf(
        "app_name" to "Faruk Music Player",
        "title" to "🎵 Faruk Music Player",
        "no_song_selected" to "No song selected",
        "artist" to "Artist",
        "volume_label" to "Volume",
        "playlist" to "Playlist",
        "shuffle" to "Shuffle",
        "previous" to "Previous",
        "play_pause" to "Play/Pause",
        "next" to "Next",
        "repeat" to "Repeat",
        "no_songs" to "No songs found",
        "loading_songs" to "Loading songs...",
        "language" to "Language: EN/TR",
        "songs_found" to "songs found"
    )
    
    fun getTranslation(key: String): String = translations[key] ?: key
}