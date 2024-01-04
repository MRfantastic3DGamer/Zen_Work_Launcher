package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.AppUsageDataEntity

@Composable
fun AppUsageDataButton (
    modifier: Modifier = Modifier,
    appUsageData: AppUsageDataEntity,
    icon: DrawablePainter,
    name: String,
    update: (AppUsageDataEntity)->Unit
){
    Row (
        modifier = modifier,
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ){
        Row (
            Modifier,
            Arrangement.Start,
        ){
            Image(painter = icon, contentDescription = "${appUsageData.`package`}-icon")
            Text(text = name)
        }
        Row (
            Modifier,
            Arrangement.End,
        ){
            IconButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.Rounded., contentDescription = )
            }
        }
    }
}