package com.example.darkmodeautochanginglivewallpaper

import android.content.res.Configuration
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.widget.Toast


class AutoChangingLiveWallpaperService : WallpaperService() {

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
        val isDarkMode = isDarkMode(newConfig)
        Toast.makeText(this, if (isDarkMode) "yes" else "no", Toast.LENGTH_SHORT).show()
        mEngine.update(isDarkMode)
    }

    private fun isDarkMode(newConfig: Configuration): Boolean {
        return newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    private inner class AutoChangingWallpaperEngine : WallpaperService.Engine(),
        SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private var mInitialized = false
        private var mIsDarkMode = false

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            mSurfaceHolder = surfaceHolder
            surfaceHolder?.addCallback(this)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mInitialized = true
            draw(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            draw(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
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
