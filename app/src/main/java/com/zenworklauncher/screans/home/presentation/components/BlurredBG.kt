package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zenworklauncher.database.preffs_database.SettingsValues

@Composable
fun BlurredBG(blurAmount: Dp, image:Int, tintKey: SettingsValues.AppsView.AppsViewKeys){
    Image(
        modifier = Modifier
            .fillMaxSize()
            .blur(
                blurAmount,
                edgeTreatment = BlurredEdgeTreatment(
                    RoundedCornerShape(0.dp)
                )
            ),
        bitmap = ImageBitmap.imageResource(id = image),
        contentDescription = "quick-apps-background",
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopStart,
    )
}