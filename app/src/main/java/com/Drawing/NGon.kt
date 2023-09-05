package com.example.launcher.Drawing

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun hexagonShape(sideLength: Dp): Shape {
    return nGonShape(sideLength,6)
}

fun nGonShape(sideLength: Dp, sides: Int): Shape {
    return object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val path = Path()
            val centerX = size.width / 2
            val centerY = size.height / 2

            val angle = 360f / sides

            for (i in 0 until sides) {
                val x = centerX + sideLength.value * cos(Math.toRadians(i * angle.toDouble())).toFloat()
                val y = centerY + sideLength.value * sin(Math.toRadians(i * angle.toDouble())).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            path.close()
            return Outline.Generic(path)
        }
    }
}