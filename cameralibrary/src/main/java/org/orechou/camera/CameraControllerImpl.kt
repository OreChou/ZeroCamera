package org.orechou.camera

import android.os.Handler
import android.os.HandlerThread
import org.orechou.camera.device.api2.CameraApi2Impl

class CameraControllerImpl : CameraController {

    private lateinit var cameraThread: HandlerThread
    private lateinit var cameraHandler: Handler

    private lateinit var cameraProxy: CameraProxy

    override fun onCreate() {
        cameraThread = HandlerThread("CameraThread")
        cameraThread.start()
        cameraHandler = Handler(cameraThread.looper)
    }

    override fun createCamera() {
        cameraProxy = CameraApi2Impl()
        cameraProxy.apply {

        }
    }

    override fun openCamera() {
        cameraProxy.openCamera()
    }

    override fun startPreview() {
        cameraProxy.startPreview()
    }

    override fun takePicture() {
        cameraProxy.takePicture()
    }

    override fun closeCamera() {
        cameraProxy.closeCamera()
    }

    override fun onDestroy() {
        cameraThread.quitSafely()
    }
}