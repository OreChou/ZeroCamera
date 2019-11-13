package org.orechou.render

import android.graphics.Rect
import android.os.Handler
import android.view.Surface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import org.orechou.support.model.TextureSurface

interface RenderController : LifecycleObserver{

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()

    fun createDisplayWindow(surface: Surface)

    fun getPreviewSurface(): TextureSurface

    fun setViewport(viewport: Rect)

    fun requestRender(renderType: Int)

    fun getHandler(): Handler

    companion object {
        const val PREVIEW_RENDER = 0
    }

}