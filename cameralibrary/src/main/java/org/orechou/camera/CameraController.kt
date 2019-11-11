package org.orechou.camera

import android.util.Size
import android.view.Surface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface CameraController : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()

    fun createCamera(surface: Surface, previewSize: Size)

    fun openCamera()

    fun createSession()

    fun changeSize(previewSize: Size)

    fun startPreview()

    fun takePicture()

    fun closeCamera()

    companion object {
        const val MSG_CREATE_CAMERA_SUCCESS = 0
        const val MES_CREATE_CAMERA_SESSION_SUCCESS = 1
    }

}