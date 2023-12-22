package com.zenworklauncher.screans.settings.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.zenworklauncher.screans.home.model.AppData

data class GroupData(
    val name: String,
    val icon: ImageVector,
    val packages: MutableList<AppData>
)
