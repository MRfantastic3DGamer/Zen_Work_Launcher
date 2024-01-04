package com.zenworklauncher.screans.home.model

import com.zenworklauncher.model.AppData

data class HomeState(
    val allAppsData: List<AppData>,
    var appSearchQuery: String,
    var searchedAppsData: List<AppData>,
)

enum class HomeFocusEnum{
    Home,
    Searching
}