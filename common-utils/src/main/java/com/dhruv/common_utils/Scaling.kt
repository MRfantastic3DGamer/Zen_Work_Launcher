package com.dhruv.common_utils

import android.util.DisplayMetrics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


/**
 * @param metrics -> getResources().getDisplayMetrics()
 */
class Scaling (
    val metrics: DisplayMetrics
){
    val dpi: Float = (metrics.xdpi + metrics.ydpi) / 2
    val densityFactor = dpi / 160

    fun intToDp(int: Int): Dp {
        return (densityFactor * int).dp
    }

    fun offsetIntToDp(offset: IntOffset): List<Dp> {
        return listOf(intToDp(offset.x), intToDp(offset.y))
    }
}