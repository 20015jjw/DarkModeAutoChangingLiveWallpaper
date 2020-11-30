package com.example.darkmodeautochanginglivewallpaper.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.darkmodeautochanginglivewallpaper.WallpaperMode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    private fun getWallpaperPath(context: Context, wallpaperMode: WallpaperMode): String {
        return context.getExternalFilesDir(null).toString() +
                File.separator +
                wallpaperMode.getFileName()
    }

    fun getWallpaperBitmap(context: Context, wallpaperMode: WallpaperMode): Bitmap {
        return BitmapFactory.decodeFile(getWallpaperPath(context, wallpaperMode))
    }

    fun doesWallpaperExist(context: Context, wallpaperMode: WallpaperMode): Boolean {
        val file = File(getWallpaperPath(context, wallpaperMode))
        return file.exists()
    }

    fun writeWallpaperBitmapToFile(context: Context, wallpaperMode: WallpaperMode, bitmap: Bitmap) {
        val file = File(getWallpaperPath(context, wallpaperMode))
        file.createNewFile()

        //Convert bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.PNG,
            0,
            byteArrayOutputStream
        ) // YOU can also save it in JPEG
        val bitmapData = byteArrayOutputStream.toByteArray()

        //write the bytes in file
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(bitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()
    }
}