package com.zenworklauncher.widgets.screans.schedule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zenworklauncher.R

@Composable
fun DayTimeVisual(
    modifier: Modifier = Modifier,
    screenHeight: Dp,
){

    val brush = Brush.verticalGradient(
        0.10f  to colorResource(R.color.black),
        0.20f  to colorResource(R.color.morning_white),
        0.41f to colorResource(R.color.sky_light_blue),
        0.7f to colorResource(R.color.noon_yellow),
        0.75f to colorResource(R.color.evening_red),
        0.80f to colorResource(R.color.black),
        startY = 0f,
        endY = 2090f,
        tileMode = TileMode.Repeated
    )

    Box(
        modifier = modifier
            .size(20.dp, screenHeight)
            .padding(vertical = 20.dp)
            .background(
                brush = brush,
                shape = RoundedCornerShape(100.dp)
            )
    ){
        val dist = 2090f / 24
    }
}