package org.orechou.camera.ui


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil

import org.orechou.camera.R
import org.orechou.camera.databinding.FragmentCameraBinding

/**
 * A simple [Fragment] subclass.
 */
class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.handler = this
        binding.lifecycleOwner = this
        return binding.root
    }

    private val surfaceViewCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }
        override fun surfaceDestroyed(holder: SurfaceHolder?) {}
        override fun surfaceCreated(holder: SurfaceHolder?) {}
    }

}
