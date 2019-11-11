package org.orechou.camera.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceView

class AutoFitSurfaceView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var size: Rect? = null

    fun setSize(rect: Rect) {
        size = rect
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // object?.let: 判断 object 不为 null 的条件下，去执行 let 函数体
        size?.let {
            setMeasuredDimension(it.width(), it.height())
        }
    }


}