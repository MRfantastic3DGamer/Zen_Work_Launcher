package com.zenworklauncher.widgets.quick_action.model

import androidx.compose.ui.graphics.vector.ImageVector

data class QuickAction(
    val imageVector: ImageVector,
    val name: String,
    val onSelect: (()->Unit)?
)