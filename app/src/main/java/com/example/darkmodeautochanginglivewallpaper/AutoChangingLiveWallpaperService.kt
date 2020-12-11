package com.example.darkmodeautochanginglivewallpaper

import android.content.SharedPreferences
import android.content.res.Configuration
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.darkmodeautochanginglivewallpaper.util.FileUtil


class AutoChangingLiveWallpaperService : WallpaperService() {

    private var mCurrentConfig: Configuration? = null
    private lateinit var mEngine: AutoChangingWallpaperEngine

    override fun onCreateEngine(): Engine {
        mEngine = AutoChangingWallpaperEngine()
        update(resources.configuration)
        return mEngine
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        update(newConfig)
    }

    private fun update(newConfig: Configuration) {
        mCurrentConfig = newConfig
        val isDarkMode = isDarkMode(newConfig)
        Toast.makeText(this, if (isDarkMode) "yes" else "no", Toast.LENGTH_SHORT).show()
        mEngine.update(isDarkMode)
    }

    fun redraw() {
        mCurrentConfig?.let { update(it) }
    }

    private fun isDarkMode(newConfig: Configuration): Boolean {
        return newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    private inner class AutoChangingWallpaperEngine : WallpaperService.Engine(),
        SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private lateinit var mPref: SharedPreferences
        private lateinit var mPrefChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
        private var mInitialized = false
        private var mIsDarkMode = false

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            mPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            mPrefChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, _: String -> redraw() }
            mPref.registerOnSharedPreferenceChangeListener(mPrefChangeListener)
            mSurfaceHolder = surfaceHolder
            surfaceHolder?.addCallback(this)
        }

        override fun onDestroy() {
            super.onDestroy()
            mPref.unregisterOnSharedPreferenceChangeListener(mPrefChangeListener)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mInitialized = true
            draw(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            draw(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mInitialized = false
        }

        fun update(isDarkMode: Boolean) {
            mIsDarkMode = isDarkMode
            draw(surfaceHolder)
        }

        private fun draw(holder: SurfaceHolder) {
            if (mInitialized) {
                val canvas = holder.lockCanvas()
                try {
                    val bmp = FileUtil.getWallpaperBitmap(
                        this@AutoChangingLiveWallpaperService,
                        if (mIsDarkMode) WallpaperMode.DARK else WallpaperMode.LIGHT
                    )
                    canvas.drawBitmap(bmp, 0F, 0F, null)
                } catch (e: Exception) {
                    canvas.drawRGB(0, 125, 125)
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

}
