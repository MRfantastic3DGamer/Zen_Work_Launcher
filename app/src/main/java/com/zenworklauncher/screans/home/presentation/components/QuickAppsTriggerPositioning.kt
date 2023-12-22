package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.dp
import com.dhruv.quick_apps.QuickAppsTrigger
import com.zenworklauncher.preffsDatabase.SettingsValues


@Composable
fun QuickAppsTriggerPositioning(
    onTriggerGloballyPositioned: (LayoutCoordinates) -> Unit,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragStop: () -> Unit
){
    Column {
        Box {
            QuickAppsTrigger(
                modifier = Modifier
                    .size(
                        SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.labelWidth),
                        SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.height)
                    ),
                onTriggerGloballyPositioned = onTriggerGloballyPositioned,
                onDragStart = onDragStart,
                onDrag = onDrag,
                onDragStop = onDragStop,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}