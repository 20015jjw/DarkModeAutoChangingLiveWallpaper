package com.example.darkmodeautochanginglivewallpaper

import android.content.Intent
import android.os.Bundle

enum class WallpaperMode {

    LIGHT, DARK;

    companion object {
        private const val KEY = "wallpaper_mode_intent_key"

        private const val LIGHT_MODE_WALLPAPER_FILE_NAME = "light_mode.png"
        private const val DARK_MODE_WALLPAPER_FILE_NAME = "dark_mode.png"

        fun extractFromIntent(intent: Intent): WallpaperMode {
            return values()[intent.getIntExtra(KEY, -1)]
        }

        fun extractFromBundle(bundle: Bundle): WallpaperMode {
            return values()[bundle.getInt(KEY)]
        }

        fun existsInIntent(intent: Intent?): Boolean {
            return intent?.hasExtra(KEY) ?: false
        }

        fun existsInBundle(bundle: Bundle?): Boolean {
            return bundle?.containsKey(KEY) ?: false
        }
    }

    fun getFileName(): String {
        return when (this) {
            LIGHT -> LIGHT_MODE_WALLPAPER_FILE_NAME
            DARK -> DARK_MODE_WALLPAPER_FILE_NAME
        }
    }

    fun putIntoIntent(intent: Intent) {
        intent.putExtra(KEY, ordinal)
    }

    fun putIntoBundle(bundle: Bundle) {
        bundle.putInt(KEY, ordinal)
    }
}
