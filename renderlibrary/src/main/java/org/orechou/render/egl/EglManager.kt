package org.orechou.render.egl

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.util.Log
import android.view.Surface
import org.orechou.render.util.GLES20Utils
import kotlin.math.log

/**
 * EglManager 可以为任意一个 Surface 创建自己的 EGL 环境。
 * EGL 是 OpenGL ES 渲染 API 和本地窗口系统（native platform window system）之间的一个中间接口层，它也主要由厂商来实现。
 */
class EglManager {
    private var mEglSurface = EGL14.EGL_NO_SURFACE
    private var mEglContext = EGL14.EGL_NO_CONTEXT
    private var mEglDisplay = EGL14.EGL_NO_DISPLAY

    /**
     * 创建 EGL 环境的步骤如下：
     * 1. 创建与本地窗口系统的连接（调用 eglGetDisplay 方法得到 EGLDisplay）
     * 2. 初始化 EGL 方法（调用 eglInitialize 方法初始化）
     * 3. 确定渲染表面的配置信息（调用 eglChooseConfig 方法得到 EGLConfig）
     * 4. 创建渲染上下文（通过 EGLDisplay 和 EGLConfig ，调用 eglCreateContext 方法创建渲染上下文，得到 EGLContext）
     * 5. 创建渲染表面（通过 EGLDisplay 和 EGLConfig ，调用 eglCreateWindowSurface 方法创建渲染表面，得到 EGLSurface）
     * 6. 绑定上下文（通过 eglMakeCurrent 方法将 EGLSurface、EGLContext、EGLDisplay 三者绑定）
     */
    fun initEGL(surface: Surface) {
//        if (mEglDisplay != EGL14.EGL_NO_SURFACE && mEglContext != EGL14.EGL_NO_CONTEXT && mEglDisplay != EGL14.EGL_NO_DISPLAY) {
//            EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)
//            return
//        }
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = IntArray(2)
        EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)
        val confAttr = intArrayOf(
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            GLES20Utils.EGL_RECORDABLE_ANDROID, 1,
            EGL14.EGL_SAMPLE_BUFFERS, 1,
            EGL14.EGL_SAMPLES, 4,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        EGL14.eglChooseConfig(mEglDisplay, confAttr, 0, configs, 0, 1, numConfigs, 0)
        val ctxAttr = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, // 0x3098
            EGL14.EGL_NONE
        )
        mEglContext = EGL14.eglCreateContext(mEglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, ctxAttr, 0)
        val surfaceAttr = intArrayOf(EGL14.EGL_NONE)
        mEglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, configs[0], surface, surfaceAttr, 0)
        EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)
        Log.d(TAG, "initEGL ${EGL14.eglGetCurrentContext()}, $mEglContext")
    }

    /**
     * 7. 交换缓冲
     * 当用 OpenGL 绘制结束后，使用 eglSwapBuffers 方法交换前后缓冲，将绘制内容显示到屏幕上。
     */
    fun swapBuffers() {
        EGL14.eglSwapBuffers(mEglDisplay, mEglSurface)
    }

    /**
     * 8. 释放 EGL 环境
     * 绘制结束，不再需要使用 EGL 时，取消 eglMakeCurrent 的绑定，销毁 EGLDisplay、EGLSurface、EGLContext
     */
    fun release() {
        Log.d(TAG, "release ${EGL14.eglGetCurrentContext()} before")
        EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
        checkError()
        if (mEglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface(mEglDisplay, mEglSurface)
            checkError()
            mEglSurface = EGL14.EGL_NO_SURFACE
        }
        if (mEglContext != EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroyContext(mEglDisplay, mEglContext)
            checkError()
            mEglContext = EGL14.EGL_NO_CONTEXT
        }
        if (mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglReleaseThread()
            checkError()
            EGL14.eglTerminate(mEglDisplay)
            checkError()
            mEglDisplay = EGL14.EGL_NO_DISPLAY
        }
        Log.d(TAG, "release ${EGL14.eglGetCurrentContext()} after")
    }

    fun makeCurrent() {
        EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)
    }

    fun getContext(): EGLContext {
        return mEglContext
    }

    private fun checkError() {
        val error = EGL14.eglGetError()
        if (error != EGL14.EGL_SUCCESS) {
            Log.d(TAG, "error: $error")
        }
    }

    companion object {
        private const val TAG = "EglManager"
    }
}