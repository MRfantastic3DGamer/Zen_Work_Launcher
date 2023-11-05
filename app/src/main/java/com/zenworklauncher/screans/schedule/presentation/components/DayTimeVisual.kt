package com.zenworklauncher.screans.schedule.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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

    val path by remember{
        mutableStateOf(getPath(dist = screenHeight.value/6, width = 170f, startX = -160f))
    }
    
    val offsets by remember{
        mutableStateOf(getOffsets(screenHeight.value/3, startX = -190f, startY = 110f))
    }

    Box(
        modifier = modifier
            .size(70.dp, screenHeight * 2)
    ) {
        Spacer(modifier = Modifier
            .drawWithCache {
                onDrawBehind {
                    drawPath(
                        path,
                        color = Color.White,
                        style = Stroke(
                            width = 120f,
                            pathEffect = PathEffect.cornerPathEffect(100f)
                        )
                    )
                }
            }
            .fillMaxSize()
        )
        for (i in 0 until 24) {
            Text(
                text = ((i % 12) + 1).toString(),
                modifier = Modifier.offset { offsets[i] },
                style = TextStyle(
                    color = Color.Red,
                    fontWeight = FontWeight.W900,
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            )
        }
    }

//    Canvas(modifier = modifier){
//        clipPath(
//            path = path,
//            clipOp = ClipOp.Intersect
//        ){
//            this.drawRect(
//                color = Color.White
//            )
//        }
//    }

//    Box(
//        modifier = modifier
//            .size(20.dp, screenHeight)
//            .padding(vertical = 20.dp)
//            .background(
//                brush = brush,
//                shape = RoundedCornerShape(100.dp)
//            )
//    ){
//
//    }
}

fun getPath(dist: Float = 200f, width: Float = 150f, startX: Float = 0f): Path {
    val path = Path()
    var x = startX
    var y = 0f
    path.moveTo(x,y)
    for( i in 0 until 24 ){
        y += dist
        x += width
        path.lineTo(x,y)
        y += dist
        x -= width
        path.lineTo(x,y)
    }
    return path
}

fun getOffsets(dist: Float = 200f, startX: Float = 0f, startY: Float = 0f): List<IntOffset> {
    val ofs = mutableListOf<IntOffset>()
    val x = startX
    var y = startY
    for( i in 0 until  24){
        ofs.add(IntOffset(x.toInt(),y.toInt()))
        y += dist
    }
    return ofs.toList()
}

@Preview()
@Composable
fun preview(){
    DayTimeVisual(screenHeight = 1200.dp)
}