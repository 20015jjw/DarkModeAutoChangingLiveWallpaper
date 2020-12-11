package com.example.darkmodeautochanginglivewallpaper

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.darkmodeautochanginglivewallpaper.constant.SharedPrefConstants
import com.example.darkmodeautochanginglivewallpaper.util.FileUtil

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val WALLPAPER_SELECT_REQUEST_CODE = 0
        const val WALLPAPER_PREVIEW_REQUEST_CODE = 1
    }

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
        private lateinit var mSharedPref: SharedPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val wallpaperSelectorPreference =
                preferenceManager.findPreference<WallpaperPreviewPreference>(
                    getString(WallpaperPreviewPreference.KEY_ID)
                )
            wallpaperSelectorPreference?.mDelegate = this

            if (WallpaperMode.existsInBundle(savedInstanceState))
                mModeInProgress = WallpaperMode.extractFromBundle(savedInstanceState!!)
        }

        override fun onImageViewClicked(wallpaperMode: WallpaperMode) {
            mModeInProgress = wallpaperMode
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
                    wallpaperMode.putIntoIntent(this)
                }
            startActivityForResult(galleryIntent, WALLPAPER_SELECT_REQUEST_CODE)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            mModeInProgress?.putIntoBundle(outState)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == WALLPAPER_SELECT_REQUEST_CODE) {
                val intent = Intent(activity, WallpaperPreviewActivity::class.java).apply {
                    putExtra(WALLPAPER_PREVIEW_ACTIVITY_IMAGE_URI_KEY, data?.data.toString())
                    mModeInProgress?.putIntoIntent(this)
                }
                startActivityForResult(intent, WALLPAPER_PREVIEW_REQUEST_CODE)
            } else if (requestCode == WALLPAPER_PREVIEW_REQUEST_CODE) {
                val mode = WallpaperMode.extractFromIntent(data!!)
                val wallpaperSelectorPreference =
                    preferenceManager.findPreference<WallpaperPreviewPreference>(
                        getString(
                            WallpaperPreviewPreference.KEY_ID
                        )
                    )
                val bitmap = FileUtil.getWallpaperBitmap(requireContext(), mode)
                when (mode) {
                    WallpaperMode.DARK ->
                        wallpaperSelectorPreference?.mDarkModeImageView?.setImageBitmap(bitmap)
                    WallpaperMode.LIGHT ->
                        wallpaperSelectorPreference?.mLightModeImageView?.setImageBitmap(bitmap)
                }
                mSharedPref.edit()
                    .putLong(SharedPrefConstants.UPDATE_TIME, System.currentTimeMillis()).apply()
                mModeInProgress = null
            }
        }
    }

}