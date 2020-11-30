package com.example.darkmodeautochanginglivewallpaper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.example.darkmodeautochanginglivewallpaper.util.FileUtil
import com.github.chrisbanes.photoview.PhotoView


const val WALLPAPER_PREVIEW_ACTIVITY_IMAGE_URI_KEY = "wallpaper_preview_activity_image_uri_key"

class WallpaperPreviewActivity : Activity() {

    companion object {
        private const val MAX_SCALE = 8F
    }

    private var mImageUri: Uri? = null

    private lateinit var mPhotoView: PhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallpaper_preview_activity)
        mImageUri = Uri.parse(intent.getStringExtra(WALLPAPER_PREVIEW_ACTIVITY_IMAGE_URI_KEY))

        if (mImageUri == null) {
            Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            mPhotoView = findViewById(R.id.photo_view)
            mPhotoView.setImageURI(mImageUri)
            mPhotoView.maximumScale = MAX_SCALE
            mPhotoView.setOnClickListener {

                val bitmap = mPhotoView.drawToBitmap()
                try {
                    val mode = WallpaperMode.extractFromIntent(intent)
                    FileUtil.writeWallpaperBitmapToFile(this, mode, bitmap)
                    val resultIntent = Intent().apply { mode.putIntoIntent(this) }
                    setResult(0, resultIntent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        window.decorView.systemUiVisibility = newUiOptions
    }

}