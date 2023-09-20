package com.zenworklauncher.widgets.quick_action.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zenworklauncher.widgets.quick_action.model.QuickAction

@Composable
fun QuickActionItem(
    modifier: Modifier = Modifier,
    action: QuickAction,
    selected: Boolean
) {
    val size = animateDpAsState(targetValue = if(selected) 45.dp else 30.dp, label = "size for action : ${action.name}")
    Surface(
        modifier = modifier
            .size(size.value),
        color = Color.White,
        shape = CircleShape,
        shadowElevation = if(selected) 5.dp else 10.dp,
        tonalElevation = if(selected) 5.dp else 10.dp,
    ) {
        Image(imageVector = action.imageVector, contentDescription = action.name)
    }
}