package org.orechou.camera.utils

import android.graphics.Rect
import android.util.Log
import org.orechou.support.utils.ScreenUtils
import kotlin.math.max
import kotlin.math.min

object CameraUtils {
    private const val TAG = "CameraUtils"
    // the number of four kind screen size.
    const val SCREEN_FULL_RATIO = 0
    const val SCREEN_16_TO_9_RATIO = 1
    const val SCREEN_4_TO_3_RATIO = 2
    const val SCREEN_1_TO_1_RATIO = 3

    // The preview screen ratio, full, 16:9, 4:3, 1:1
    private val screenFullRect: Rect
    private val screen19To9Rect: Rect
    private val screen4To3Rect: Rect
    private val screen1To1Rect: Rect

    init {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        Log.d(TAG, "The screen width: $screenWidth, height: $screenHeight")
        val screenLongSide = max(screenWidth, screenHeight)
        val screenShortSide = min(screenWidth, screenHeight)

        /**
         * TODO: Should dynamic calculate the location of the left top point in screen.
         */
        screenFullRect = Rect(0, 0, screenShortSide, screenLongSide)
        screen19To9Rect = Rect(0, 0, screenShortSide, screenShortSide * 16 / 9)
        screen4To3Rect = Rect(0, 0, screenShortSide, screenShortSide * 4 / 3)
        screen1To1Rect = Rect(0, 0, screenShortSide, screenShortSide)
        Log.d(TAG, "Screen full size: $screenShortSide * $screenLongSide")
        Log.d(TAG, "Screen 16:9 size: $screenShortSide * ${screenShortSide * 16 / 9}")
        Log.d(TAG, "Screen 4:3 size: $screenShortSide * ${screenShortSide * 4 / 3}")
        Log.d(TAG, "Screen 1:1 size: $screenShortSide * $screenShortSide")
    }

    fun getScreenSize(ratio: Int): Rect {
        return when (ratio) {
            SCREEN_FULL_RATIO -> screenFullRect
            SCREEN_16_TO_9_RATIO -> screen19To9Rect
            SCREEN_4_TO_3_RATIO -> screen4To3Rect
            SCREEN_1_TO_1_RATIO -> screen1To1Rect
            else -> screen4To3Rect
        }
    }

}
