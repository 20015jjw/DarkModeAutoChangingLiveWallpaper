package com.example.darkmodeautochanginglivewallpaper.util

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.WindowManager

object DisplayUtil {
    private fun getDisplay(context: Context): Display? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            context.display
        } else {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
    }

    fun generateRatioString(context: Context): String {
        val size = Point()
        getDisplay(context)?.getRealSize(size)
        return "W,${size.x}:${size.y}"
    }
}
