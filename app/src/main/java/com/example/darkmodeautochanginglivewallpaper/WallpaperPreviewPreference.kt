package com.example.darkmodeautochanginglivewallpaper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder


class WallpaperPreviewPreference : Preference {

    private var mHolder: ConstraintLayout? = null
    var mLightModeImageView: ImageView? = null
    var mDarkModeImageView: ImageView? = null

    lateinit var mDelegate: Delegate

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
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