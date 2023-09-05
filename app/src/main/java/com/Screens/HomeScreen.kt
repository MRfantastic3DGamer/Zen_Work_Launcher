package com.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.Buttons.QuickAppsZone

@Composable
fun HomeScreen() {
    QuickAppsZone(
        modifier = Modifier,
        numberOfApps = 10,
    ){
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Yellow)
//        )
    }
}