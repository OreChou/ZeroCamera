package org.orechou.camera

import android.graphics.ImageFormat
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import org.orechou.camera.CameraController.Companion.MES_CREATE_CAMERA_SESSION_SUCCESS
import org.orechou.camera.CameraController.Companion.MSG_CREATE_CAMERA_SUCCESS
import org.orechou.camera.device.CameraFactory

class CameraControllerImpl : CameraController {
    private lateinit var cameraThread: HandlerThread
    private lateinit var cameraHandler: Handler

    private var cameraProxy: CameraProxy? = null
    // The preview surface will not change.
    private var previewSurface: Surface? = null
    private var saveImageReader: ImageReader? = null

    override fun onCreate() {
        cameraThread = HandlerThread("CameraThread")
        cameraThread.start()
        cameraHandler = Handler(cameraThread.looper) {
            when (it.what) {
                MSG_CREATE_CAMERA_SUCCESS -> {
                    Log.d(TAG, "Open camera success.")
                    createSession()
                }
                MES_CREATE_CAMERA_SESSION_SUCCESS -> {
                    Log.d(TAG, "Create session success.")
                    startPreview()
                }
            }
            false
        }
        Log.d(TAG, "CameraController onCreate: create camera thread success.")
    }

    override fun createCamera(surface: Surface, previewSize: Size) {
        cameraHandler.post {
            previewSurface = surface
            saveImageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 2)
            cameraProxy = CameraFactory.provideCameraProxyApi2(previewSurface!!, saveImageReader!!.surface, previewSize, cameraHandler)
        }
    }

    override fun openCamera() {
        cameraHandler.post { cameraProxy?.openCamera() }
    }

    override fun createSession() {
        cameraHandler.post { cameraProxy?.createSession() }
    }

    override fun changeSize(previewSize: Size) {
        closeCamera()
        createCamera(previewSurface!!, previewSize)
        openCamera()
    }

    override fun startPreview() {
        cameraHandler.post { cameraProxy?.startPreview() }
    }

    override fun takePicture() {
        cameraHandler.post { cameraProxy?.takePicture() }
    }

    override fun closeCamera() {
        cameraHandler.post {
            cameraProxy?.closeCamera()
            saveImageReader?.close()
        }
    }

    override fun onDestroy() {
        cameraHandler.post { cameraThread.quitSafely() }
    }

    companion object {
        private const val TAG = "CameraController"
    }
}