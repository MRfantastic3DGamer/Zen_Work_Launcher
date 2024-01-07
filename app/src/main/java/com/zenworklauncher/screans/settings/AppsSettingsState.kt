package com.zenworklauncher.screans.settings

import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.AppUsageDataEntity

data class AppsSettingsState(
    val allAppsUsageData    : List<AppUsageDataEntity>,
    val icons               : List<DrawablePainter>,
    val names               : List<String>,
    val allGroups           : List<String>,
)