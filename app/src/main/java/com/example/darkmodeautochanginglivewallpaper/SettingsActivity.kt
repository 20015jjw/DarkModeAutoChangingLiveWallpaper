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
        private var mModeInProgress: WallpaperMode? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val wallpaperSelectorPreference =
                preferenceManager.findPreference<WallpaperPreviewPreference>(getString(R.string.wallpaper_selector_pref_key))
            wallpaperSelectorPreference?.mDelegate = this

            if (savedInstanceState != null && WallpaperMode.existsInBundle(savedInstanceState))
                mModeInProgress = WallpaperMode.extractFromBundle(savedInstanceState)
        }

        override fun onImageViewClicked(wallpaperMode: WallpaperMode) {
            mModeInProgress = wallpaperMode
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
                    wallpaperMode.putIntoIntent(this)
                }
            startActivityForResult(galleryIntent, 0)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            if (mModeInProgress != null) {
                mModeInProgress!!.putIntoBundle(outState)
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 0) {
                val intent = Intent(activity, WallpaperPreviewActivity::class.java).apply {
                    putExtra(WALLPAPER_PREVIEW_ACTIVITY_IMAGE_URI_KEY, data?.data.toString())
                    mModeInProgress?.putIntoIntent(this)
                }
                startActivityForResult(intent, 1)
            } else if (requestCode == 1) {
                val mode = WallpaperMode.extractFromIntent(data!!)
                val wallpaperSelectorPreference =
                    preferenceManager.findPreference<WallpaperPreviewPreference>(getString(R.string.wallpaper_selector_pref_key))
                val bitmap = FileUtil.getWallpaperBitmap(requireContext(), mode)
                when (mode) {
                    WallpaperMode.DARK ->
                        wallpaperSelectorPreference?.mDarkModeImageView?.setImageBitmap(bitmap)
                    WallpaperMode.LIGHT ->
                        wallpaperSelectorPreference?.mLightModeImageView?.setImageBitmap(bitmap)
                }
                mModeInProgress = null
            }
        }
    }

}