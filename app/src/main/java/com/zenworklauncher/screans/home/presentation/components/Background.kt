package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.zenworklauncher.R

// TODO: add some interactions like animations to make it seem more interactive
@Composable
fun Background (

){
    Image(
        modifier = Modifier.fillMaxSize(),
        bitmap = ImageBitmap.imageResource(id = R.drawable.wallpaper),
        contentDescription = "wallpaper",
        contentScale = ContentScale.Crop
    )
}