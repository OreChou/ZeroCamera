package org.orechou.camera.ui

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.fragment_camera.*
import org.orechou.camera.CameraController
import org.orechou.camera.CameraControllerImpl

import org.orechou.camera.R
import org.orechou.camera.databinding.FragmentCameraBinding
import org.orechou.camera.utils.CameraUtils

class CameraFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCameraBinding

    private lateinit var cameraController: CameraController

    private var previewSizeRatio = CameraUtils.SCREEN_16_TO_9_RATIO

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.handler = this
        binding.lifecycleOwner = this
        binding.surfaceView.setSize(CameraUtils.getScreenSize(previewSizeRatio))
        binding.surfaceView.holder.addCallback(surfaceViewCallback)
        cameraController = CameraControllerImpl()
        this.lifecycle.addObserver(cameraController)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mIvSize -> {
                when (previewSizeRatio) {
                    CameraUtils.SCREEN_FULL_RATIO -> previewSizeRatio = CameraUtils.SCREEN_16_TO_9_RATIO
                    CameraUtils.SCREEN_16_TO_9_RATIO -> previewSizeRatio = CameraUtils.SCREEN_4_TO_3_RATIO
                    CameraUtils.SCREEN_4_TO_3_RATIO -> previewSizeRatio = CameraUtils.SCREEN_1_TO_1_RATIO
                    CameraUtils.SCREEN_1_TO_1_RATIO -> previewSizeRatio = CameraUtils.SCREEN_FULL_RATIO
                }
                surfaceView.setSize(CameraUtils.getScreenSize(previewSizeRatio))
            }
        }
    }

    private val surfaceViewCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.d(TAG, "SurfaceView width: $width, height: $height")
            val previewSize = Size(width, height)
            cameraController.apply {
                closeCamera()
                createCamera(holder!!.surface, previewSize)
                openCamera()
            }
        }
        override fun surfaceDestroyed(holder: SurfaceHolder?) {}
        override fun surfaceCreated(holder: SurfaceHolder?) {}
    }

    companion object {
        private const val TAG = "CameraFragment"
    }
}
