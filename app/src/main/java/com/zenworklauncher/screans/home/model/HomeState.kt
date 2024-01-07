package com.zenworklauncher.screans.home.model

import com.zenworklauncher.model.AppUIData

data class HomeState(
    val allAppsData: List<AppUIData>,
    var appSearchQuery: String,
    var searchedAppsData: List<AppUIData>,
)

enum class HomeFocusEnum{
    Home,
    Searching
}