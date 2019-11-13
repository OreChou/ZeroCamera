package org.orechou.camera.task

import android.media.Image
import org.orechou.support.utils.ContextUtils
import org.orechou.support.utils.TimeUtils
import java.io.File
import java.io.FileOutputStream

class ImageSaveTask(private val image: Image): Runnable {

    override fun run() {
        val saveFile = File(ContextUtils.getApplication().getExternalFilesDir(null), "IMAGE_${TimeUtils.getTimeStringFormat()}.jpg")
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        FileOutputStream(saveFile).apply {
            write(bytes)
            flush()
            close()
        }
        image.close()
    }

}