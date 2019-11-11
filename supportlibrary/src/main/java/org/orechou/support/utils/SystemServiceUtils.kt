package org.orechou.support.utils

import android.content.Context
import android.hardware.camera2.CameraManager
import android.view.WindowManager

object SystemServiceUtils {

    fun getCameraManager(): CameraManager {
        return ContextUtils.getApplicationContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    fun getWindowManager(): WindowManager {
        return ContextUtils.getApplicationContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

}