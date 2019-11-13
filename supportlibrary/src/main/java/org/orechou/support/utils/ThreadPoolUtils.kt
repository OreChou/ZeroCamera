package org.orechou.support.utils

import java.util.concurrent.Executors

object ThreadPoolUtils {

    private val threadPool = Executors.newCachedThreadPool()

    fun execute(runnable: Runnable) {
        threadPool.execute(runnable)
    }

}