package org.orechou.support.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context

object ContextUtils {

    private var application: Application

    init {
        application = getApplicationByReflect()
    }

    fun getApplication(): Application {
        return application
    }

    fun getApplicationContext(): Context {
        return application.applicationContext
    }

    fun getActivity(): Activity {
        return getTopActivityByReflect()!!
    }

    @SuppressLint("PrivateApi")
    private fun getApplicationByReflect(): Application {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val thread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val app = activityThreadClass.getMethod("getApplication").invoke(thread) ?: throw NullPointerException("u should init first")
        application = app as Application
        return application
    }

    @SuppressLint("PrivateApi")
    private fun getTopActivityByReflect(): Activity? {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
        mActivityListField.isAccessible = true
        val activities = mActivityListField.get(currentActivityThreadMethod) as Map<*, *>
        for (activityRecord in activities.values) {
            val activityRecordClass = activityRecord!!.javaClass
            val pausedField = activityRecordClass.getDeclaredField("paused")
            pausedField.isAccessible = true
            if (!pausedField.getBoolean(activityRecord)) {
                val activityField = activityRecordClass.getDeclaredField("activity")
                activityField.isAccessible = true
                return activityField.get(activityRecord) as Activity
            }
        }
        return null
    }

}