package org.orechou.camera.device

import android.os.Handler
import android.util.Size
import android.view.Surface
import org.orechou.camera.CameraProxy
import org.orechou.camera.device.api2.CameraApi2Impl

object CameraFactory {

    fun provideCameraProxyApi2(previewSurface: Surface, saveImageSurface: Surface, previewSize: Size, cameraHandler: Handler): CameraProxy {
        val cameraProxy = CameraApi2Impl(cameraHandler)
        cameraProxy.apply {
            setPreviewSurface(previewSurface)
            setSaveImageSurface(saveImageSurface)
            setPreviewSize(previewSize)
        }
        return cameraProxy
    }

}