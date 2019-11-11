package org.orechou.zero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.orechou.camera.ui.CameraFragment
import org.orechou.support.utils.ScreenUtils

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        ScreenUtils.setFullScreen(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.content, CameraFragment())
        ft.commit()
    }
}
