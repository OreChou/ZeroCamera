package org.orechou.camera.device.api2

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import org.orechou.camera.device.CameraProxy
import org.orechou.camera.task.ImageSaveTask
import org.orechou.support.utils.ScreenUtils
import org.orechou.support.utils.SystemServiceUtils
import org.orechou.support.utils.ThreadPoolUtils

/**
 * The implement of [CameraProxy] by Camera API2, only do things as follows:
 * 1. Connect the CameraDevice, operate camera.
 * 2. It will be always running in camera separate thread.
 */
class CameraApi2Impl(private val cameraHandler: Handler) : CameraProxy {

    // 0: means front, 1: means back
    private var cameraFacing = CameraCharacteristics.LENS_FACING_BACK
    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var sensorOrientation = 0
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null

    private var previewSurface: Surface? = null
    private var saveSizeRatio: Float? = null
    private var saveImageReader: ImageReader? = null
    private var saveImageSurface: Surface? = null

    override fun openCamera(previewSurface: Surface, saveSizeRatio: Float) {
        this.previewSurface = previewSurface
        this.saveSizeRatio = saveSizeRatio
        openCameraInternal()
    }

    override fun changeFacing() {
        cameraFacing = if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) CameraCharacteristics.LENS_FACING_FRONT else CameraCharacteristics.LENS_FACING_BACK
        openCameraInternal()
    }

    @SuppressLint("MissingPermission")
    private fun openCameraInternal() {
        val cameraManager = SystemServiceUtils.getCameraManager()
        for (cameraId in cameraManager.cameraIdList) {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics?.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                sensorOrientation = cameraCharacteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                cameraManager.openCamera(cameraId, deviceStateCallback, null)
            }
        }
    }

    override fun createSession() {
        var largestSize: Size? = null
        val map = cameraCharacteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        for (size in listOf(*map!!.getOutputSizes(ImageFormat.JPEG))) {
            val ratio = size.width.toFloat() / size.height
            if (ratio == saveSizeRatio) {
                if (largestSize == null) {
                    largestSize = size
                } else {
                    if (size.width * size.height > largestSize.width * largestSize.height) {
                        largestSize = size
                    }
                }
            }
        }
        saveImageReader = ImageReader.newInstance(largestSize!!.width, largestSize.height, ImageFormat.JPEG, 2)
        saveImageReader?.setOnImageAvailableListener(onImageAvailableListener, cameraHandler)
        this.saveImageSurface = saveImageReader?.surface
        cameraDevice?.createCaptureSession(listOf(previewSurface, saveImageSurface), sessionStateCallback, null)
    }

    override fun startPreview() {
        val previewRequest = previewRequestBuilder?.apply {
            addTarget(previewSurface!!)
            set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
        }!!.build()
        cameraSession?.setRepeatingRequest(previewRequest, null, null)
    }

    override fun stopPreview() {
        cameraSession?.apply { stopRepeating() }
    }

    override fun takePicture() {
        val captureRequest = captureRequestBuilder?.apply {
            addTarget(saveImageSurface!!)
            set(CaptureRequest.JPEG_ORIENTATION, (ORIENTATIONS.get(ScreenUtils.getScreenRotation()) + sensorOrientation + 270) % 360)
            set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            set(CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
        }!!.build()
        cameraSession?.apply {
            stopRepeating()
            abortCaptures()
            capture(captureRequest, captureCallback,null)
        }
    }

    override fun startRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeCamera() {
        cameraDevice?.close()
        cameraSession?.close()
        saveImageReader?.close()
    }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createSession()
        }
        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }
        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
        }
    }

    private val sessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraSession = session
            previewRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            startPreview()
        }
        override fun onConfigureFailed(session: CameraCaptureSession) {}
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            Log.d("captureCallback", "Capture success")
            startPreview()
        }
    }

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        cameraHandler.post {
            ThreadPoolUtils.execute(ImageSaveTask(it.acquireNextImage()!!))
        }
    }

    companion object {
        private val ORIENTATIONS = SparseIntArray()
        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
}