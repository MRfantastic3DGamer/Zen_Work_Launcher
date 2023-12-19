package com.zenworklauncher.utils

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun Outline(path: Path, width: Float, blendMode: BlendMode = BlendMode.Overlay){
    Canvas(
        modifier = Modifier
    ){
        this.drawPath(
            path,
            Color.White,
            style = Stroke(
                width = width
            ),
            blendMode = blendMode
        )
    }
}