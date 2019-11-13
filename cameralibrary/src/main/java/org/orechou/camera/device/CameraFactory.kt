package org.orechou.camera.device

import android.os.Handler
import android.view.Surface
import org.orechou.camera.device.api2.CameraApi2Impl

object CameraFactory {

    fun provideCameraProxyApi2(cameraHandler: Handler): CameraProxy {
        return CameraApi2Impl(cameraHandler)
    }

}