package org.orechou.zero.frgments

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.fragment_camera.*
import org.orechou.camera.CameraController
import org.orechou.camera.CameraControllerImpl
import org.orechou.camera.utils.CameraUtils
import org.orechou.render.RenderController
import org.orechou.render.RenderController.Companion.PREVIEW_RENDER
import org.orechou.render.RenderControllerImpl
import org.orechou.support.model.TextureSurface
import org.orechou.zero.R
import org.orechou.zero.databinding.FragmentCameraBinding

class CameraFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentCameraBinding

    private lateinit var cameraController: CameraController
    private lateinit var renderController: RenderController

    private lateinit var previewSurface: TextureSurface
    private var previewSizeRatio = CameraUtils.SCREEN_4_TO_3_RATIO

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.handler = this
        binding.lifecycleOwner = this

        cameraController = CameraControllerImpl()
        renderController = RenderControllerImpl()
        this.lifecycle.addObserver(cameraController)
        this.lifecycle.addObserver(renderController)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (binding.surfaceView != null) {
            binding.surfaceView.setSize(CameraUtils.getScreenSize(previewSizeRatio))
            binding.surfaceView.holder.addCallback(surfaceViewCallback)
        }
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
                val viewport = CameraUtils.getScreenSize(previewSizeRatio)
                surfaceView.setSize(viewport)
                renderController.setViewport(viewport)
            }
            R.id.btnShutter -> {
                cameraController.takePicture()
            }
            R.id.btnFacing -> {
                cameraController.changeFacing()
            }
        }
    }

    private val surfaceViewCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.d(TAG, "surfaceChanged")
            Log.d(TAG, "width: $width, height: $height")
            previewSurface.surfaceTexture.setDefaultBufferSize(height, width)
            cameraController.apply {
                closeCamera()
                createCamera()
                openCamera(previewSurface.surface, height.toFloat() / width)
            }
        }
        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            Log.d(TAG, "surfaceDestroyed")
        }
        // Home & Back will recall surfaceCreated, the OpenGL Context will rebuild
        override fun surfaceCreated(holder: SurfaceHolder?) {
            Log.d(TAG, "surfaceCreated")
            renderController.createDisplayWindow(holder!!.surface)
            previewSurface = renderController.getPreviewSurface()
            previewSurface.surfaceTexture.setOnFrameAvailableListener(onFrameAvailable, renderController.getHandler())
        }
    }

    private val onFrameAvailable = SurfaceTexture.OnFrameAvailableListener {
//        Log.d(TAG, "onFrameAvailable")
        renderController.requestRender(PREVIEW_RENDER)
    }

    companion object {
        private const val TAG = "CameraFragment"
    }
}
