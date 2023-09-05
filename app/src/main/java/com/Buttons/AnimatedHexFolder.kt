package com.example.launcher.Buttons

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.Buttons.EmptyHex
import com.example.launcher.Drawing.hexagonShape
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

@Preview
@Composable
fun AnimatedHexFolder(){
    val opened = remember {
        mutableStateOf(true)
    }
    val showApps = remember {
        mutableStateOf(true)
    }
    val offset = IntOffset.Zero
    val angle = 360f / 6
    val rounds : Int = 2
    val sideLength : Dp = 100.dp
    val separation : Float = 150F
    val appPoints: MutableList<Pair<Float, Float>> = mutableListOf()

    if(rounds > 0) {
        for (i in 0 until 6) {
            val x: Float =
                ((sideLength.value + separation * 0.4) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                ((sideLength.value + separation * 0.4) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
        }
    }
    if(rounds > 1) {
        for (i in 0 until 6) {
            val x: Float =
                ((sideLength.value + separation * 1.2) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                ((sideLength.value + separation * 1.2) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
        }
        for (i in 0 until 6) {
            val x: Float =
                ((sideLength.value + separation * 1) * cos(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                ((sideLength.value + separation * 1) * sin(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
        }
    }
    appPoints.sortWith(compareBy { it.second * 100 + it.first })

    val openVal by animateFloatAsState(
        targetValue = if(opened.value) 1F else 0F, label = "open-val",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
            ),
        finishedListener = {
                showApps.value = (it == 1F)
            }
        )
    var c = 0
    if(showApps.value){
        for (i in 0 until appPoints.size){
            EmptyHex(
                sideLength = sideLength,
                offset = IntOffset.Zero + IntOffset(
                    offset.x + appPoints[i].first.toInt(),
                    offset.y + appPoints[i].second.toInt()) * max(0F,openVal),
                color = Color.Gray
            )
            c++
        }
    }

    Box(modifier = Modifier
        .offset { IntOffset.Zero }
        .size(sideLength, sideLength * 2)
        .clip(hexagonShape(sideLength + 5.dp))
        .background(Color.White)
        .pointerInput(Unit) {
            detectTapGestures {
                showApps.value = true
                opened.value = !opened.value
            }
        },
    ){
        Text(text = "$c", modifier = Modifier.align(Alignment.Center), color = Color.Black)
    }
}
