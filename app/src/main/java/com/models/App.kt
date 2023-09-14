package com.models

import androidx.compose.ui.graphics.painter.Painter

data class App(
    val id: Int = 0,
    val name: String,
    val icon: Painter,
    val packageName: String,
    val noOfTimesOpened: Int,
)