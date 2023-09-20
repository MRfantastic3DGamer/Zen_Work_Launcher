package com.zenworklauncher.widgets.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.zenworklauncher.widgets.utils.WidgetsScreenType

class WidgetsScreenViewModel : ViewModel() {
    var currentScreen : WidgetsScreenType by mutableStateOf(WidgetsScreenType.Schedule)
    var currentScreenOffset: IntOffset by mutableStateOf(HomeOffset)
    val screenVisibility: Map<WidgetsScreenType,Boolean> by mutableStateOf(
        mapOf(
            pairs = arrayOf(
                WidgetsScreenType.Home to false,
                WidgetsScreenType.Schedule to true,
                WidgetsScreenType.Entertainment to false,
            )
        )
    )
    fun TransitionToScreen(type: WidgetsScreenType): WidgetsScreenType? {
        if(type == currentScreen) return null
        val prev = currentScreen
        currentScreen = type
        when (type) {
            WidgetsScreenType.Home -> currentScreenOffset = HomeOffset
            WidgetsScreenType.Schedule -> currentScreenOffset = ScheduleOffset
            WidgetsScreenType.Entertainment -> currentScreenOffset = EntertainmentOffset
        }
        return prev
    }

    companion object{
        val HomeOffset = IntOffset(0,0)
        val ScheduleOffset = IntOffset(1000,0)
        val EntertainmentOffset = IntOffset(0,1000)
    }
}