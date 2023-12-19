package com.zenworklauncher.screans.home.model

import androidx.compose.ui.graphics.vector.ImageVector

data class WidgetsTabData(
    val name: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val ids: List<Int>,
)
