package org.orechou.render.util

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import org.orechou.render.shader.BaseHandle
import javax.microedition.khronos.opengles.GL10

object GLES20Utils {

    private const val TAG = "GLES20Utils"
    const val EGL_RECORDABLE_ANDROID = 0x3142

    /**
     * 生成纹理一共有 4 步：
     * 1. 生成纹理名称：glGenTextures
     * 2. 绑定纹理名称到指定激活的纹理单元中（current active texture unit）：glBindTexture
     * 3. 创建参数:
     *      3.1 设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
     *      3.2 设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
     *      3.3 设置环绕方向 S，截取纹理坐标到 [1/2n,1-1/2n], 将导致永远不会与 border 融合
     *      3.4 设置环绕方向T，截取纹理坐标到 [1/2n,1-1/2n], 将导致永远不会与 border 融合
     * 4. 解绑纹理与激活的纹理单元: glBindTexture
     */
    fun createOESTexture(): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        return texture[0]
    }

    fun createTexture(): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return texture[0]
    }

//    private fun loadShader(shaderType: Int, source: String): Int {
//        var shader = GLES20.glCreateShader(shaderType)
//        if (shader != 0) {
//            GLES20.glShaderSource(shader, source)
//            GLES20.glCompileShader(shader)
//            val compiled = IntArray(1)
//            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
//            if (compiled[0] == 0) {
//                Log.e(TAG, "Could not compile shader $shaderType:")
//                Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
//                GLES20.glDeleteShader(shader)
//                shader = 0
//            }
//        }
//        return shader
//    }
//
//    fun linkProgram(vertexSource: String, fragmentSource: String): Int {
//        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
//        if (vertexShader == 0) {
//            return 0
//        }
//        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
//        if (fragmentShader == 0) {
//            return 0
//        }
//        var program = GLES20.glCreateProgram()
//        if (program != 0) {
//            GLES20.glAttachShader(program, vertexShader)
//            checkError("glAttachShader")
//            GLES20.glAttachShader(program, fragmentShader)
//            checkError("glAttachShader")
//            GLES20.glLinkProgram(program)
//            val linkStatus = IntArray(1)
//            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
//            if (linkStatus[0] != GLES20.GL_TRUE) {
//                Log.e(TAG, "Could not link program: ")
//                Log.e(TAG, GLES20.glGetProgramInfoLog(program))
//                GLES20.glDeleteProgram(program)
//                program = 0
//            }
//        }
//        return program
//    }

    fun loadShader(shaderType: Int, source: String): Int {
        var shader = GLES20.glCreateShader(shaderType)
        checkError()
        GLES20.glShaderSource(shader, source)
        checkError()
        GLES20.glCompileShader(shader)
        checkError()
        return shader
    }

    fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {
        val program = GLES20.glCreateProgram()
        checkError()
        if (program == 0) {
            Log.d(TAG, "Cannot create OpenGL program: ${GLES20.glGetError()}")
        }
        GLES20.glAttachShader(program, vertexShader)
        checkError("glAttachShader")
        GLES20.glAttachShader(program, fragmentShader)
        checkError("glAttachShader")
        GLES20.glLinkProgram(program)
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program: ${GLES20.glGetProgramInfoLog(program)}")
            GLES20.glDeleteProgram(program)
        }
        return program
    }

    fun useProgram(program: Int) {
        GLES20.glUseProgram(program)
    }

    fun loadHandle(program: Int, baseHandles: MutableCollection<BaseHandle>) {
        for (handle in baseHandles) {
            handle.load(program)
        }
    }

    fun clear() {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_STENCIL_BUFFER_BIT)
    }

    fun getAttribLocation(program: Int, name: String): Int {
        return GLES20.glGetAttribLocation(program, name)
    }

    fun getUniformLocation(program: Int, name: String): Int {
        return GLES20.glGetUniformLocation(program, name)
    }

    private fun checkError() {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.d(TAG, "GL error: $error, ${Throwable()}")
        }
    }

    private fun checkError(label: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.d(TAG, "$label error: $error, ${Throwable()}")
        }
    }

}