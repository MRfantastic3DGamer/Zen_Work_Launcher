package com.example.launcher.Buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.launcher.Drawing.hexagonShape
import com.example.launcher.models.AppButtonData
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun EmptyHex(sideLength: Dp, oX: Float, oY:Float, color: Color = Color.Black){
    Box(
        modifier = Modifier
            .offset { IntOffset(oX.roundToInt(), oY.roundToInt()) }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(color),
    )
}

@Composable
fun EmptyHex(sideLength: Dp, offset: IntOffset, color: Color = Color.Black){
    Box(
        modifier = Modifier
            .offset { offset }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(color),
    )
}

@Composable
fun AppHex(sideLength: Dp, oX: Float, oY:Float, icon: Painter){
    val upperBound   = - 500
    val lowerBound   =  2100
    val leftBound    = - 400
    val rightBound   =  1300

    if(oX < leftBound || oX > rightBound || oY < upperBound || oY > lowerBound) return

    Box(
        modifier = Modifier
            .offset { IntOffset(oX.roundToInt(), oY.roundToInt()) }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(Color.Black),
    ){
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.4F),
            painter = icon,
            contentDescription = "app_icon")
//        Text(text = "$appIndex",
//            modifier = Modifier.align(Alignment.Center),
//            color = Color.White)
    }
}

@Composable
fun AppHex(sideLength: Dp, offset: IntOffset, icon: Painter){
    val upperBound   = - 500
    val lowerBound   =  2100
    val leftBound    = - 400
    val rightBound   =  1300

    if(offset.x < leftBound || offset.x > rightBound || offset.y < upperBound || offset.y > lowerBound) return

    Box(
        modifier = Modifier
            .offset { offset }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(Color.Black),
    ){
        Image(
            modifier = Modifier
                .align(Alignment.Center)
//                .fillMaxSize(0.4F)
                .scale(0.5F),
            painter = icon,
            contentDescription = "app_icon")

//        Text(text = "$appIndex",
//            modifier = Modifier.align(Alignment.Center),
//            color = Color.White)
    }
}

@Composable
fun FolderHex(sideLength: Dp, oX: Float, oY:Float, folderName: String){
    val upperBound   = - 500
    val lowerBound   =  2100
    val leftBound    = - 400
    val rightBound   =  1300

    if(oX < leftBound || oX > rightBound || oY < upperBound || oY > lowerBound) return

    Box(
        modifier = Modifier
            .offset { IntOffset(oX.roundToInt(), oY.roundToInt()) }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(Color.Black),
    ){
        Text(text = folderName,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White)
    }
}

@Composable
fun FolderHex(sideLength: Dp, offset: IntOffset, folderName: String){
    val upperBound   = - 500
    val lowerBound   =  2100
    val leftBound    = - 400
    val rightBound   =  1300

    if(offset.x < leftBound || offset.x > rightBound || offset.y < upperBound || offset.y > lowerBound) return

    Box(
        modifier = Modifier
            .offset { offset }
            .size(sideLength, sideLength * 2)
            .clip(hexagonShape(sideLength + 5.dp))
            .background(Color.Black),
    ){
        Text(text = folderName,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White)
    }
}

@Composable
fun folderBody(sideLength: Dp, separation: Float, oX: Float, oY: Float, rounds: Int, appCount: Int){
    val angle = 360f / 6
    var distance = 0f
    var appPoints: MutableList<Pair<Float, Float>> = mutableListOf()

    EmptyHex(sideLength = (sideLength.value + 1).dp, oX = oX, oY = oY, color = Color.Gray )
    if(rounds > 0) {
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 0.4) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 0.4) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
            EmptyHex(sideLength = (sideLength.value + 1).dp, oX = x, oY = y, color = Color.Gray)
        }
    }
    if(rounds > 1) {
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 1.2) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 1.2) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
        }
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 1) * cos(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 1) * sin(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
        }
    }
    appPoints.sortWith(compareBy { it.second * 100 + it.first })
    for (i in 0 until  appCount){
        Text(text = i.toString(), modifier = Modifier
            .offset {
                IntOffset(
                    (oX + appPoints[i].first).toInt(),
                    (oY + appPoints[i].second).toInt()
                )
            }
            .background(Color.Green)
        )
    }
}

@Composable
fun Folder(sideLength: Dp, separation: Float, oX: Float, oY: Float, rounds: Int, appButtons: MutableList<AppButtonData>) {

    val angle = 360f / 6
    var distance = 0f
    var appPoints: MutableList<Pair<Float, Float>> = mutableListOf()

    EmptyHex(sideLength = (sideLength.value + 1).dp, oX = oX, oY = oY, color = Color.Gray )
    if(rounds > 0) {
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 0.4) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 0.4) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
            EmptyHex(sideLength = (sideLength.value + 1).dp, oX = x, oY = y, color = Color.Gray)
        }
    }
    if(rounds > 1) {
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 1.2) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 1.2) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
            EmptyHex(sideLength = (sideLength.value + 1).dp, oX = x, oY = y, color = Color.Gray)
        }
        for (i in 0 until 6) {
            val x: Float =
                (oX + (sideLength.value + separation * 1) * cos(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            val y: Float =
                (oY + (sideLength.value + separation * 1) * sin(Math.toRadians(i * angle.toDouble())).toFloat()).toFloat()
            appPoints.add(Pair(x,y))
            EmptyHex(sideLength = (sideLength.value + 1).dp, oX = x, oY = y, color = Color.Gray)
        }
    }
    appPoints.sortWith(compareBy { it.second * 100 + it.first })
    for (i in 0 until  appButtons.size){
        AppHex(sideLength = sideLength, oX = appPoints[i].first, oY = appPoints[i].second, icon = appButtons[i].app.icon)
    }
}

@Preview
@Composable
fun Preview(){

    var opened = remember{ mutableStateOf(true) }

    val angle = 360f / 6
    val rounds : Int = 2
    val sideLength : Dp = 100.dp
    val separation : Float = 150F
    val appPoints: MutableList<Pair<Float, Float>> = mutableListOf()

    Box(modifier = Modifier
        .fillMaxSize()
        .offset(x = 200.dp, y = 200.dp)
        ) {
        if(rounds > 0) {
            for (i in 0 until 6) {
                val x: Float =
                    ((sideLength.value + separation * 0.4) * cos(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
                val y: Float =
                    ((sideLength.value + separation * 0.4) * sin(Math.toRadians((i + 0.5) * angle.toDouble())).toFloat()).toFloat()
                appPoints.add(Pair(x,y))
//                EmptyHex(sideLength = (sideLength.value + 1).dp, oX = x, oY = y, color = Color.Gray)
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
        for (i in 0 until  appPoints.size){
            EmptyHex(
                sideLength = sideLength,
                offset = IntOffset((appPoints[i].first - 110).toInt(), (appPoints[i].second - 250).toInt()))
            Text(text = i.toString(),
                modifier = Modifier
                .offset { IntOffset((appPoints[i].first).toInt(), (appPoints[i].second).toInt()) },
                color = Color.White
            )
        }
    }
}