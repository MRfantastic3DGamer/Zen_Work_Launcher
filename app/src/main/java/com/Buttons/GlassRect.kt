package com.Buttons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlassRect(
    opened: Boolean,
){
    Column(
        modifier = Modifier
            .width(120.dp)
            .height(146.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
//        Cloudy {
//            Card(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
//            ) {
//
//            }
//        }
        Box(modifier = Modifier.size(120.dp))
        Text(
            text = "folder",
            style = TextStyle(
                fontSize = 25.sp, // 30
                lineHeight = 35.67.sp,
//                fontFamily = FontFamily(Font(R.font.kalam)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun GlassFolder(){
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val counts = arrayOf(3,5,7,8,10,12,14)

    val offsets by remember { mutableStateOf(calculateOffsets(
        width = screenWidth.value * 2.5f,
        startingRadius = 180f,
        rounds = 7,
        count = counts
    )) }

    Box(
        modifier = Modifier
            .width(screenWidth)
            .height(screenWidth)
            .padding(10.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 1000.dp,
                    topEnd = 30.dp,
                    bottomEnd = 30.dp,
                    bottomStart = 70.dp
                )
            )
//            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(topStart = 60.dp))
                .background(Color.Gray)
                .fillMaxSize()
        )
        val rounds = 5
        val radiuses = arrayOf(120,230,340,450,100)
        var I = 0
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            for (i in 0 until counts.sum()){
                drawCircle(
                    color = Color.White,
                    radius = 50f,
                    center = Offset(screenWidth.value*2.4f,screenWidth.value*2.4f) - offsets[i]
                )
            }
        })
//        for (i in 0 until count.sum()){
//            Box(
//                modifier = Modifier
//                    .size(50.dp)
//                    .offset {
//                        IntOffset(
//                            screenWidth.value.toInt() * 2,
//                            screenWidth.value.toInt() * 2
//                        ) - offsets[i]
//                    }
//                    .clip(CircleShape)
//                    .background(Color.Red)
//            ){
//                Text(text = i.toString())
//            }
//        }
    }
}

fun calculateOffsets(width: Float, startingRadius: Float, rounds: Int = 6, count: Array<Int> = arrayOf(2,4,6,8,9)): List<Offset> {
    var offsets = mutableListOf<Offset>()
    val radiuses = mutableListOf<Float>()
    for (i in 0 until rounds){
        radiuses.add(startingRadius + ((width-startingRadius)/rounds)*i)
    }
    for (r in 0 until rounds){
        val angle = (90.0/(count[r]-1))/180 * PI
        var currAngle = 0.0
        for (c in 0 until count[r]){
            offsets.add(Offset(
                (cos(currAngle) * radiuses[r]).toFloat(),
                (sin(currAngle) * radiuses[r]).toFloat(),
            ))
            currAngle += angle
        }
    }
    return offsets.toList()
}

@Preview()
@Composable
fun preview(){
    Box(
        modifier = Modifier
            .background(Color.Black)
    ) {
        GlassFolder()
    }
}