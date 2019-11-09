package org.orechou.support.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.IBinder

object ContextUtils {

    private lateinit var application: Application

    fun getApp(): Application {
        return if (application != null) application else getApplicationByReflect()
    }

    fun getApplicationContext(): Context {
        return getApp().applicationContext
    }

    private fun getApplicationByReflect(): Application {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val thread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val app = activityThreadClass.getMethod("getApplication").invoke(thread) ?: throw NullPointerException("u should init first")
        application = app as Application
        return application
    }

//    private fun getActivityByReflect(): Activity {
//        val activityThreadClass  = Class.forName("android.app.ActivityThread")
//        val activityThread = activityThreadClass .getMethod("currentActivityThread").invoke(null)
//        val activitiesField = activityThreadClass.getDeclaredField("mActivities")
//        activitiesField.isAccessible = true
//        val activities = activitiesField.get(activityThread)
//    }

}