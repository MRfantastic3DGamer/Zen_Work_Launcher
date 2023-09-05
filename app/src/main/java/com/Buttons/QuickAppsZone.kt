package com.Buttons

import android.graphics.Point
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun QuickAppsZone(
    modifier: Modifier = Modifier,
    numberOfApps: Int,
    content: (@Composable ()->Unit)?,
){

    var selected by remember { mutableStateOf(2) }
    val A by remember { mutableStateOf(Point(-1,0)) }
    val B by remember { mutableStateOf(Point(0,0)) }
    val C by remember { mutableStateOf(Point(0,0)) }

    val icons: List<IconInRadialFolder> = mutableListOf() // get from provider

    var currentAngle by remember{ mutableStateOf(0F) }
    var quickAppsOffset by remember{ mutableStateOf(IntOffset(0,0)) }
    var quickAppsOpened by remember { mutableStateOf(false) }
    val quickAppFunctions by remember { mutableStateOf(getIconFunctions(icons)) }
    var animatedAngles = getAnimatedAngles(numberOfApps,selected)
    var animatedSize = getAnimatedSizes(numberOfApps,selected)

    Box (
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { it ->
                        quickAppsOpened = true
                        quickAppsOffset = it.round()
                        C.x = 0
                        C.y = 0
                    },
                    onDrag = { _, offset ->
                        C.x += offset.x.toInt()
                        C.y += offset.y.toInt()
                        currentAngle = 275 - calculateAngle(A, B, C).toFloat()
                        selected = currentAngle.toInt() / (180 / numberOfApps)
                    },
                    onDragEnd = { quickAppsOpened = false },
                    onDragCancel = { quickAppsOpened = false }
                )
            }
    ){
        Column {
            Text(text = "selected : $selected")
            Text(text = "currentAngle : $currentAngle")
        }
        if(quickAppsOpened){
            for (i in 0 until numberOfApps){
                Box (
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                quickAppsOffset.x + (cos((animatedAngles[i].value + 90) * Math.PI / 180) * 500F).toInt(),
                                quickAppsOffset.y + (sin((animatedAngles[i].value + 90) * Math.PI / 180) * 500F).toInt()
                            )
                        }
                        .size(animatedSize[i].value)
                        .clip(CircleShape)
                        .background(if (i == selected) Color.Blue else Color.White)
                ){
                    Text(text = animatedAngles[i].value.toString())
                }
            }
        }

        content?.invoke()
    }
}


data class IconInRadialFolder(
    val packageName: String,
    val name: String,
    val onSelect: (()->Unit)?
)

fun calculateAngle(a: Point, b: Point, c: Point): Double {
    val ab: Double = sqrt((b.x - a.x).toDouble().pow(2.0) + (b.y - a.y).toDouble().pow(2.0))
    val bc: Double = sqrt((c.x - b.x).toDouble().pow(2.0) + (c.y - b.y).toDouble().pow(2.0))
    val ac: Double = sqrt((c.x - a.x).toDouble().pow(2.0) + (c.y - a.y).toDouble().pow(2.0))
    val ratio : Double = (ab * ab + ac * ac - bc * bc) /( 2 * ac * ab)
    var degre = acos(ratio) *(180/Math.PI)
    if(c.y > b.y) degre = 360 - degre
    return degre
}

@Composable
fun getAnimatedAngles(n:Int, selected: Int): MutableList<State<Float>> {
    val angles = mutableListOf<State<Float>>()
    var a = 0F
    var v = 0
    for(i in 0 until n){
        v = abs(i-selected)
        a += if(v<2) if(v==0) 25F else 20F else 15F
        angles.add(animateFloatAsState(targetValue = a, label = "animated angle for $i"))
    }
    return angles
}

@Composable
fun getAnimatedSizes(n:Int, selected: Int): MutableList<State<Dp>> {
    val angles = mutableListOf<State<Dp>>()
    var a = 50.dp
    var v = 0
    for(i in 0 until n){
        v = abs(i-selected)
        a = if(v<2) if(v==0) 70.dp else 60.dp else 50.dp
        angles.add(animateDpAsState(targetValue = a, label = "animated angle for $i"))
    }
    return angles
}

fun getIconFunctions(icons: List<IconInRadialFolder>): MutableList<() -> Unit> {
    val list = mutableListOf<()->Unit>()
    icons.forEach { iconData ->
        if(iconData.onSelect != null) list.add(iconData.onSelect)
        else list.add { println(iconData.packageName) }
    }
    return list
}