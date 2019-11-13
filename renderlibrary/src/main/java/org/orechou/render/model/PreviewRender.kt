package org.orechou.render.model

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import org.orechou.render.util.GLES20Utils
import org.orechou.support.model.TextureSurface

open class PreviewRender : Render {
    // Handle
    private var program  = 0
    private var aPosition  = 0
    private var aTextureCoord  = 0
    private var uTextureMatrix  = 0
    private var uTextureSampler  = 0

    override fun prepare() {
        val vertexShader = GLES20Utils.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SOURCE)
        val fragmentShader = GLES20Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SOURCE)
        program = GLES20Utils.linkProgram(vertexShader, fragmentShader)
        GLES20.glUseProgram(program)
        aPosition = GLES20.glGetAttribLocation(program, "aPosition")
        aTextureCoord = GLES20.glGetAttribLocation(program, "aTextureCoord")
        uTextureMatrix = GLES20.glGetUniformLocation(program, "uTextureMatrix")
        uTextureSampler = GLES20.glGetUniformLocation(program, "uTextureSampler")
    }

    override fun draw(textureSurface: TextureSurface) {
        val transformMatrix = FloatArray(16)
        textureSurface.surfaceTexture.updateTexImage()
        textureSurface.surfaceTexture.getTransformMatrix(transformMatrix)

        // step1: clear
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glClearColor(0f, 0f, 0f, 0f)

        // step2: load preview texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureSurface.textureId)
        GLES20.glUniform1i(uTextureSampler, 0)
        GLES20.glUniformMatrix4fv(uTextureMatrix, 1, false, transformMatrix, 0)

        // step3: load vertex data
        VertexBuffer.vertexBuffer.position(0)
        GLES20.glEnableVertexAttribArray(aPosition)
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 16, VertexBuffer.vertexBuffer)
        VertexBuffer.vertexBuffer.position(2)
        GLES20.glEnableVertexAttribArray(aTextureCoord)
        GLES20.glVertexAttribPointer(aTextureCoord, 2, GLES20.GL_FLOAT, false, 16, VertexBuffer.vertexBuffer)

        // step4: draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        // step5: release
        GLES20.glDisableVertexAttribArray(aPosition)
        GLES20.glDisableVertexAttribArray(aTextureCoord)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        GLES20.glFinish()
    }

    override fun finish() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private const val TAG = "PreviewRender"

        private const val VERTEX_SOURCE = "attribute vec4 aPosition;\n" +
                "attribute vec4 aTextureCoord;\n" +
                "\n" +
                "uniform mat4 uTextureMatrix;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main() {\n" +
                "    vTextureCoord = (uTextureMatrix * aTextureCoord).xy;\n" +
                "    gl_Position = aPosition;\n" +
                "}"

        private const val FRAGMENT_SOURCE = "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "\n" +
                "uniform samplerExternalOES uTextureSampler;\n" +
                "varying vec2 vTextureCoord;\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);\n" +
                "    gl_FragColor = vCameraColor;" +
                "}\n"
    }
}