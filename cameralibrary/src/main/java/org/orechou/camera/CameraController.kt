package org.orechou.camera

import android.view.Surface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface CameraController : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()

    /**
     * The method only new instance of CameraProxy
     */
    fun createCamera()

    /**
     * The method do three things:
     * 1. openCamera
     * 2. createSession
     * 3. startPreview
     */
    fun openCamera(previewSurface: Surface, imageRatio: Float)

    /**
     * Change camera facing won't change surface and image ratio
     */
    fun changeFacing()

    fun closeCamera()

    fun takePicture()

    fun startRecording()

    fun stopRecording()

}