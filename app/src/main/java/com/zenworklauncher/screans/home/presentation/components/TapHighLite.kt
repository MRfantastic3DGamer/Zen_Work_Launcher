package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun TapHighLite(touchPosition: Offset, focus: Float){
    val mag = (1-focus) * 0.07f
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.radialGradient(
                colorStops = arrayOf(
                    0f                                  to Color(1.0f, 1.0f, 1.0f, 0.5f),
                    0.12f-mag                           to Color(1.0f, 1.0f, 1.0f, 0.5f),
                    0.13f-mag                           to Color(1.0f, 1.0f, 1.0f, 0.8f),
                    0.14f-mag                           to Color(1.0f, 1.0f, 1.0f, 0.5f),
                    0.145f-mag                          to Color(1.0f, 1.0f, 1.0f, 1f),
                    0.15f-mag                           to Color(1.0f, 1.0f, 1.0f, 0f),
                ),
                center = touchPosition
            )
        )
    )
}