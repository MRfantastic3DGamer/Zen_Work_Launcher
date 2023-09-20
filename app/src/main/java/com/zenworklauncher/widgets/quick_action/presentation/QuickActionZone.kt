package com.zenworklauncher.widgets.quick_action.presentation

import android.graphics.Point
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import com.zenworklauncher.widgets.quick_action.QuickActionViewModel
import com.zenworklauncher.widgets.quick_action.presentation.components.QuickActionItem
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun QuickActionZone(
    modifier: Modifier = Modifier,
    quickActionViewModel: QuickActionViewModel,
    content: @Composable ()->Unit,
){

    var selected by remember { mutableStateOf(2) }
    val A by remember { mutableStateOf(Point(-1,0)) }
    val B by remember { mutableStateOf(Point(0,0)) }
    val C by remember { mutableStateOf(Point(0,0)) }

    val numberOfActions: Int by remember { mutableStateOf(quickActionViewModel.icons.size) }
    val icons by remember { mutableStateOf(quickActionViewModel.icons) }

    var currentAngle by remember{ mutableStateOf(0F) }
    var quickAppsOffset by remember{ mutableStateOf(IntOffset(0,0)) }
    var quickAppsOpened by remember { mutableStateOf(false) }
    val animatedAngles = getAngles(numberOfActions,selected)

    Box (
        modifier = modifier
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
                        selected = max(min(if(numberOfActions == 0) 0 else currentAngle.toInt() / (180 / numberOfActions) , icons.size-1 ), 0)
                    },
                    onDragEnd = {
                        quickAppsOpened = false
                        icons[selected].onSelect?.invoke()
                    },
                    onDragCancel = {
                        quickAppsOpened = false
                        icons[selected].onSelect?.invoke()
                    }
                )
            }
    )

    content()

    Box(modifier = modifier){
        val radios = 400F
        if(quickAppsOpened){
            for (i in 0 until numberOfActions){
                QuickActionItem(
                    modifier = Modifier
                        .offset {
                        IntOffset(
                            quickAppsOffset.x + (cos((animatedAngles[i] + 90) * Math.PI / 180) * radios).toInt(),
                            quickAppsOffset.y + (sin((animatedAngles[i] + 90) * Math.PI / 180) * radios).toInt()
                        )
                    },
                    action = icons[i],
                    selected = i == selected
                )
            }
        }
    }
}


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
fun getAngles(n:Int, selected: Int): MutableList<Float> {
    val angles = mutableListOf<Float>()
    val min = 180F/( if(n <= 1) 1 else n-1 )
    var a = -min
    for(i in 0 until n){
        a += min
        angles.add(a)
    }
    return angles
}