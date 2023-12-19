package com.zenworklauncher.screans.home.model

data class HomeState(
    val allAppsData: List<AppData>,
    var appSearchQuery: String,
    var searchedAppsData: List<AppData>,
)

enum class HomeFocusEnum{
    Home,
    Searching
}