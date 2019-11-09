package org.orechou.camera

import android.util.Size
import android.view.Surface

/**
 * 规范定义 Camera 的操作
 */
interface CameraProxy {

    fun setCameraFacing(facing: Int)

    fun setPreviewSize(size: Size)

    fun setPreviewSurface(surface: Surface)

    fun setSaveImageSurface(surface: Surface)

    fun openCamera()

    fun closeCamera()

    fun createSession()

    fun startPreview()

    fun stopPreview()

    fun takePicture()

    fun startRecording()

    fun stopRecording()

}