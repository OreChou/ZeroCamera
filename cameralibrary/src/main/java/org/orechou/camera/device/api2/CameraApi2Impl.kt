package org.orechou.camera.device.api2

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.Surface
import org.orechou.camera.CameraController
import org.orechou.camera.CameraProxy
import org.orechou.support.utils.SystemServiceUtils

/**
 * The implement of [CameraProxy] by Camera API2, only do things as follows:
 * 1. Connect the CameraDevice, operate camera.
 * 2. It will be always running in camera separate thread.
 */
class CameraApi2Impl(private val cameraHandler: Handler) : CameraProxy {

    private var cameraFacing = CameraCharacteristics.LENS_FACING_BACK
    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null

    private var previewSurface: Surface? = null
    private var saveImageSurface: Surface? = null
    private var previewSize: Size? = null

    override fun setCameraFacing(facing: Int) {
        cameraFacing = facing
    }

    override fun setPreviewSize(size: Size) {
        previewSize = size
    }

    override fun setPreviewSurface(surface: Surface) {
        previewSurface = surface
    }

    override fun setSaveImageSurface(surface: Surface) {
        saveImageSurface = surface
    }

    @SuppressLint("MissingPermission")
    override fun openCamera() {
        val cameraManager = SystemServiceUtils.getCameraManager()
        for (cameraId in cameraManager.cameraIdList) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                cameraManager.openCamera(cameraId, deviceStateCallback, null)
            }
        }
    }

    override fun closeCamera() {
        cameraDevice?.close()
        cameraSession?.close()
    }

    override fun createSession() {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takePicture() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            cameraHandler.sendEmptyMessage(CameraController.MSG_CREATE_CAMERA_SUCCESS)
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
            cameraHandler.sendEmptyMessage(CameraController.MES_CREATE_CAMERA_SESSION_SUCCESS)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}