package org.orechou.camera.device

import android.view.Surface

/**
 * The CameraProxy is a proxy of Camera API2 implementation,
 * we do not think is switch different camera, when changing facing, size.
 */
interface CameraProxy {

    fun openCamera(previewSurface: Surface, saveSizeRatio: Float)

    fun createSession()

    fun startPreview()

    fun stopPreview()

    fun changeFacing()

    fun takePicture()

    fun startRecording()

    fun stopRecording()

    fun closeCamera()
}