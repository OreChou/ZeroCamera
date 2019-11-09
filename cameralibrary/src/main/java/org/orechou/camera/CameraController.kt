package org.orechou.camera

interface CameraController {

    fun onCreate()

    fun onDestroy()

    fun createCamera()

    fun openCamera()

    fun startPreview()

    fun takePicture()

    fun closeCamera()

    companion object {
        const val MSG_CREATE_CAMERA_SUCCESS = 0
        const val MES_CREATE_CAMERA_SESSION_SUCCESS = 1
    }

}