package com.example.darkmodeautochanginglivewallpaper

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), WallpaperPreviewPreference.Delegate {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val wallpaperSelectorPreference =
                preferenceManager.findPreference<WallpaperPreviewPreference>(getString(R.string.wallpaper_selector_pref_key))
            wallpaperSelectorPreference?.mDelegate = this
        }

        override fun onImageViewClicked() {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 0)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val wallpaperSelectorPreference =
                preferenceManager.findPreference<WallpaperPreviewPreference>(getString(R.string.wallpaper_selector_pref_key))
            if (requestCode == 0) {
                wallpaperSelectorPreference?.mStartImageView?.setImageURI(data?.data)
            }
        }
    }

}