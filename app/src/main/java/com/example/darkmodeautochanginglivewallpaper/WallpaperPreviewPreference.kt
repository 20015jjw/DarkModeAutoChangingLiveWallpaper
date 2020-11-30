package com.example.darkmodeautochanginglivewallpaper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.example.darkmodeautochanginglivewallpaper.util.DisplayUtil
import com.example.darkmodeautochanginglivewallpaper.util.FileUtil


class WallpaperPreviewPreference : Preference {

    companion object {
        const val KEY_ID = R.string.wallpaper_selector_pref_key
    }

    private var mHolder: ConstraintLayout? = null
    var mLightModeImageView: ImageView? = null
    var mDarkModeImageView: ImageView? = null

    lateinit var mDelegate: Delegate

    @Suppress("unused")
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    @Suppress("unused")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @Suppress("unused")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    @Suppress("unused")
    constructor(context: Context?) : super(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val view = holder?.itemView
        mHolder = view as ConstraintLayout
        mLightModeImageView = view.findViewById(R.id.light_mode_image_view)
        mDarkModeImageView = view.findViewById(R.id.dark_mode_image_view)

        mLightModeImageView?.setOnClickListener {
            mDelegate.onImageViewClicked(WallpaperMode.LIGHT)
        }

        mDarkModeImageView?.setOnClickListener {
            mDelegate.onImageViewClicked(WallpaperMode.DARK)
        }

        setImageViewRatio()
        setImageViewContent()

    }

    private fun setImageViewRatio() {
        if (mHolder != null && mLightModeImageView != null && mDarkModeImageView != null) {
            val set = ConstraintSet()
            set.clone(mHolder)
            for (imageView in listOf(mLightModeImageView, mDarkModeImageView)) {
                set.setDimensionRatio(imageView!!.id, DisplayUtil.generateRatioString(context))
            }
            set.applyTo(mHolder)
        }
    }

    private fun setImageViewContent() {
        if (mHolder != null && mLightModeImageView != null && mDarkModeImageView != null) {
            if (FileUtil.doesWallpaperExist(context, WallpaperMode.LIGHT))
                mLightModeImageView!!.setImageBitmap(
                    FileUtil.getWallpaperBitmap(
                        context,
                        WallpaperMode.LIGHT
                    )
                )
            if (FileUtil.doesWallpaperExist(context, WallpaperMode.DARK))
                mDarkModeImageView!!.setImageBitmap(
                    FileUtil.getWallpaperBitmap(
                        context,
                        WallpaperMode.DARK
                    )
                )
        }
    }

    interface Delegate {
        fun onImageViewClicked(wallpaperMode: WallpaperMode)
    }
}