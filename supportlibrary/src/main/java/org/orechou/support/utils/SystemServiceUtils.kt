package org.orechou.support.utils

import android.content.Context
import android.hardware.camera2.CameraManager

object SystemServiceUtils {

    fun getCameraManager(): CameraManager {
        return ContextUtils.getApplicationContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

}