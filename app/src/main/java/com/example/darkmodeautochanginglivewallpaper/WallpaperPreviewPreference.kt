package com.example.darkmodeautochanginglivewallpaper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder


class WallpaperPreviewPreference : Preference {

    private var mHolder: ConstraintLayout? = null
    var mStartImageView: ImageView? = null
    private var mEndImageView: ImageView? = null

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
        mStartImageView = view.findViewById(R.id.StartImageView)
        mEndImageView = view.findViewById(R.id.EndImageView)

        mStartImageView?.setOnClickListener {
            mDelegate.onImageViewClicked()
        }

        mEndImageView?.setOnClickListener {
            Toast.makeText(context, "end clicked", Toast.LENGTH_SHORT).show()
        }

        setImageViewRatio()

    }

    private fun setImageViewRatio() {
        if (mHolder != null && mStartImageView != null && mEndImageView != null) {
            val set = ConstraintSet()
            set.clone(mHolder)
            for (imageView in listOf(mStartImageView, mEndImageView)) {
                set.setDimensionRatio(imageView!!.id, DisplayUtil.generateRatioString(context))
            }
            set.applyTo(mHolder)
        }
    }

    interface Delegate {
        fun onImageViewClicked()
    }
}