package org.orechou.render

import android.graphics.Rect
import android.opengl.GLES20
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import org.orechou.render.RenderController.Companion.PREVIEW_RENDER
import org.orechou.render.egl.EglManager
import org.orechou.render.model.PreviewRender
import org.orechou.support.model.TextureSurface
import org.orechou.render.util.GLES20Utils

class RenderControllerImpl : RenderController {
    private var isForeground = false

    private lateinit var renderThread: HandlerThread
    private lateinit var renderHandler: Handler

    // Egl relative
    private var eglManager: EglManager? = null

    // Texture relative
    private var oesTextureId = 0
    private var previewSurface: TextureSurface? = null

    // Render Collections
    private val previewRender = PreviewRender()
    private var currentRenderType = -1

    override fun onStart() {
        renderThread = HandlerThread("RenderThread")
        renderThread.start()
        renderHandler = Handler(renderThread.looper)
        // create oes preview surface in onCreate method.
        renderHandler.post {
            eglManager = EglManager()
            createPreviewSurface()
        }
    }

    override fun onStop() {
        renderHandler.post {
            eglManager?.release()
            eglManager = null
        }
        renderThread.quit()
    }

    override fun onPause() {
        isForeground = false
    }

    override fun onResume() {
        isForeground = true
        createPreviewSurface()
    }

    override fun createDisplayWindow(surface: Surface) {
        renderHandler.post {
            eglManager?.initEGL(surface)
        }
    }

    override fun getPreviewSurface(): TextureSurface {
        return previewSurface!!
    }

    override fun setViewport(viewport: Rect) {
        renderHandler.post { GLES20.glViewport(viewport.left, viewport.top, viewport.right, viewport.height()) }
    }

    override fun requestRender(renderType: Int) {
        renderHandler.post {
            if (!isForeground) {
                previewSurface?.surfaceTexture?.updateTexImage()
                return@post
            }
            when (renderType) {
                PREVIEW_RENDER -> {
                    previewSurface?.let {
                        previewRender.prepare()
                        previewRender.draw(previewSurface!!)
                        eglManager?.swapBuffers()
                    }
                }
            }
        }
    }

    override fun getHandler(): Handler {
        return renderHandler
    }

    private fun createPreviewSurface() {
        Log.d(TAG, "createPreviewSurface")
        renderHandler.post {
            oesTextureId = GLES20Utils.createOESTexture()
            previewSurface = TextureSurface(oesTextureId)
        }
    }

    companion object {
        private const val TAG = "RenderController"
    }
}