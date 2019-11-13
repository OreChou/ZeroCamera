package org.orechou.support.utils

import android.app.Activity
import android.graphics.Point
import android.view.WindowManager

object ScreenUtils {

    fun getScreenWidth(): Int {
        val windowManager = SystemServiceUtils.getWindowManager()
        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
        return point.x
    }

    fun getScreenHeight(): Int {
        val windowManager = SystemServiceUtils.getWindowManager()
        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
        return point.y
    }

    fun getScreenRotation(): Int {
        return SystemServiceUtils.getWindowManager().defaultDisplay.rotation
    }

    fun setFullScreen() {
        ContextUtils.getActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun setFullScreen(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}