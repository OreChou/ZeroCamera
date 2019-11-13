package org.orechou.support.model

import android.graphics.SurfaceTexture
import android.view.Surface

/**
 * TextureSurface is a package includes:
 * 1. OpenGL Texture Id
 * 2. SurfaceTexture
 */
data class TextureSurface(val textureId: Int) {
    val surfaceTexture = SurfaceTexture(textureId)
    val surface = Surface(surfaceTexture)
}