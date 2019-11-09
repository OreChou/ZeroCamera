package org.orechou.camera.ui

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

class AutoFitSurfaceView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var mRatio = RATIO_16_9
    var mWidth = 0
    var mHeight = 0

    fun setAspectRatio(ratio: Int) {
        mRatio = ratio
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        var ratio = 0f
        when (mRatio) {
            RATIO_16_9 -> ratio = 16f / 9f
            RATIO_1_1 -> ratio = 1f
            RATIO_4_3 -> ratio = 4f / 3f
        }
        setMeasuredDimension(width, height)
        if (width < height) {
            mWidth = width
            mHeight = (width * ratio).toInt()

        } else {
            mWidth = (height * ratio).toInt()
            mHeight = height
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    companion object {
        const val RATIO_16_9 = 0
        const val RATIO_1_1 = 1
        const val RATIO_4_3 = 2
    }

}