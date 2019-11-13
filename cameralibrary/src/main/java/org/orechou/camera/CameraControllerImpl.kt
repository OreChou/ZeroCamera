package org.orechou.camera

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import org.orechou.camera.device.CameraFactory
import org.orechou.camera.device.CameraProxy
import org.orechou.camera.device.api2.CameraApi2Impl

class CameraControllerImpl : CameraController {
    private lateinit var cameraThread: HandlerThread
    private lateinit var cameraHandler: Handler
    private var cameraProxy: CameraProxy? = null

    override fun onCreate() {
        cameraThread = HandlerThread("CameraThread")
        cameraThread.start()
        cameraHandler = Handler(cameraThread.looper)
        Log.d(TAG, "CameraController onCreate: create camera thread success.")
    }

    override fun onPause() {
        cameraHandler.post {
            cameraProxy?.stopPreview()
        }
    }

    override fun onResume() {
        cameraHandler.post {
            cameraProxy?.startPreview()
        }
    }

    override fun createCamera() {
        cameraHandler.post {
            if (cameraProxy == null || cameraProxy !is CameraApi2Impl) {
                cameraProxy = CameraFactory.provideCameraProxyApi2(cameraHandler)
            }
        }
    }

    override fun openCamera(previewSurface: Surface, imageRatio: Float) {
        cameraHandler.post {
            cameraProxy?.openCamera(previewSurface, imageRatio)
        }
    }

    override fun changeFacing() {
        closeCamera()
        cameraHandler.post {
            cameraProxy?.changeFacing()
        }
    }

    override fun takePicture() {
        cameraHandler.post { cameraProxy?.takePicture() }
    }

    override fun startRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeCamera() {
        cameraHandler.post {
            cameraProxy?.closeCamera()
        }
    }

    override fun onDestroy() {
        cameraHandler.post { cameraThread.quitSafely() }
    }

    companion object {
        private const val TAG = "CameraController"
    }
}