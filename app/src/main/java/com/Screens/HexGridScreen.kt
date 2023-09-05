package com.example.launcher.Screens

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.launcher.Buttons.AppHex
import com.example.launcher.Buttons.FolderHex
import com.example.launcher.Functions.positionToIndex
import com.example.launcher.models.AppButtonData
import com.example.launcher.models.FolderButtonData
import com.example.launcher.models.HexAction

@Composable
fun HexGridScreen(
    appButtons: MutableList<AppButtonData>,
    folders: MutableList<FolderButtonData>,
    separation: Int,
    buttonSize: Dp,
    rowSize: Int,
    onClickAction:(Int)-> Unit,

    ){

    val rightLimit = -3600F
    val leftLimit = 720
    val topLimit = 0
    val bottomLimit = 0

    val swipePowerX = remember { mutableStateOf(0F) }
    val swipePowerY = remember { mutableStateOf(0F) }
    val offsetX = remember { mutableStateOf<Float>(0F) }
    val offsetY = remember { mutableStateOf<Float>(0F) }
    val bgBrush = Brush.horizontalGradient(colors = listOf(
        Color.Black,
        Color.Gray,
        Color.Black,
    ))
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(bgBrush)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                offsetX.value += dragAmount.x * 1.5F
                offsetY.value += dragAmount.y * 1.5F
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    run {
                        val index: Int = positionToIndex(
                            offset,
                            offsetX.value,
                            offsetY.value,
                            separation,
                            buttonSize
                        )
                        onClickAction(index)
                    }
                },
                onLongPress = { offset ->
                    run {
                        println("long tap $offset")
                    }
                },
            )
        },
    ){
        appButtons.forEach { appButtons->
            AppHex(
                sideLength = buttonSize,
                oX = appButtons.posX + offsetX.value,
                oY = appButtons.posY + offsetY.value * 0.47F,
                icon = appButtons.app.icon,
            )
        }
        folders.forEach { folder ->
            FolderHex(
                sideLength = buttonSize,
                oX = folder.posX + offsetX.value,
                oY = folder.posY + offsetY.value * 0.47F,
                folderName = folder.folderName)
        }
        Text(text = "${offsetX.value}",
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, (3500 + offsetY.value).toInt()) }
                .background(Color.Green))
    }
}

@Preview
@Composable
fun priview(){
    val bgBrush = Brush.horizontalGradient(colors = listOf(
        Color.Black,
        Color.Gray,
        Color.Black,
    ))
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan)
    ){
        Text(text = "border",
            modifier = Modifier.offset { IntOffset((  -100).toInt(), 10) }
                .background(Color.Green))
    }
}