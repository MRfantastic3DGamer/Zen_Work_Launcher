package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dhruv.quick_apps.Action
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.model.AppData
import com.zenworklauncher.utils.Outline

@Composable
fun AppIcon (action: Action, offset: IntOffset, selected: Boolean, iconSizeOffset: IntOffset, iconSize: Float){
    val iconOffset = offset + iconSizeOffset
    Box(
        modifier = Modifier
            .offset { iconOffset }
            .clip(CircleShape)
            .size(iconSize.dp)
            .paint(
                (action as AppData).painter,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            )
    )
    val path = Path().apply {
        this.addRoundRect(
            RoundRect(
            iconOffset.x.toFloat(),
            iconOffset.y.toFloat(),
            iconOffset.x + iconSize*2.45f,
            iconOffset.y + iconSize*2.45f,
            radiusX = 1000f,
            radiusY = 1000f)
        )
    }
    Outline(
        path,
        if(selected)  SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.selectedIconBorderSize)
        else SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconBorderSize),
        if(selected) BlendMode.Hardlight else BlendMode.Overlay
    )
}

@Composable
fun AppIcon (action: Action, iconSize: Float){
//    Column ( ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(iconSize.dp)
                .paint(
                    (action as AppData).painter,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                )
        )
//        Text(text = action.name)
//    }
}