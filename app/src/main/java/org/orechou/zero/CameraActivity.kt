package org.orechou.zero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.orechou.camera.ui.CameraFragment

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.content, CameraFragment())
        ft.commit()
    }
}
