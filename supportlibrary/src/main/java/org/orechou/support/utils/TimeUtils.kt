package org.orechou.support.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    @SuppressLint("SimpleDateFormat")
    fun getTimeStringFormat(): String {
        return SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
    }

}