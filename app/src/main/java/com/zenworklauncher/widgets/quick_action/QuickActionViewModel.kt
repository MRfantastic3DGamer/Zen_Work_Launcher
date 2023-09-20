package com.zenworklauncher.widgets.quick_action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zenworklauncher.widgets.quick_action.model.QuickAction
import com.zenworklauncher.widgets.screans.schedule.ScheduleViewModel
import com.zenworklauncher.widgets.screans.schedule.model.Task
import com.zenworklauncher.widgets.utils.WidgetsScreenType
import com.zenworklauncher.widgets.view.WidgetsScreenViewModel

class QuickActionViewModel(
    private val widgetsScreenViewModel: WidgetsScreenViewModel,
    private val scheduleViewModel: ScheduleViewModel
) : ViewModel() {
    var icons: List<QuickAction> by mutableStateOf(listOf(
        QuickAction(
            name = "update",
            imageVector = Icons.Default.ArrowDropDown,
            onSelect = {
                scheduleViewModel.getAllTasks()
                println(" updated : ${scheduleViewModel.tasksUiState.items} ")
            }
        ),
        QuickAction(
            name = "add task",
            imageVector = Icons.Default.Add,
            onSelect = {
                scheduleViewModel.setCurrentEditTask(Task(name = "", description = "", notificationTime = "", alarmTime = "",completed = false))
                println(" should add ")
            }
        ),
        QuickAction(
            name = "log",
            imageVector = Icons.Default.List,
            onSelect = {
                println("log successful")
            }
        ),
    ))


    /*
    * actions related to use of specific screens
    */
    val Use = mapOf<WidgetsScreenType,List<QuickAction>>(
        WidgetsScreenType.Home to emptyList<QuickAction>(),
        WidgetsScreenType.Schedule to listOf(
            QuickAction(
                name = "update",
                imageVector = Icons.Default.ArrowDropDown,
                onSelect = {
                    scheduleViewModel.getAllTasks()
                }
            ),
            QuickAction(
                name = "add task",
                imageVector = Icons.Default.Add,
                onSelect = {
                    scheduleViewModel.setCurrentEditTask(Task(name = "", description = "", notificationTime = "", alarmTime = "",completed = false))
                }
            ),
        )
    )

    /*
    * actions related to navigation b/w different screens
    */
    val Navigation = mapOf<WidgetsScreenType,List<QuickAction>>(
        WidgetsScreenType.Home to listOf<QuickAction>(
            QuickAction(
                name = "navigate to schedule",
                imageVector = Icons.Default.Edit,
                onSelect = {
                    widgetsScreenViewModel.TransitionToScreen(WidgetsScreenType.Schedule)
                }
            )
        ),

        WidgetsScreenType.Schedule to listOf<QuickAction>(
            QuickAction(
                name = "navigate to home",
                imageVector = Icons.Default.Home,
                onSelect = {
                    widgetsScreenViewModel.TransitionToScreen(WidgetsScreenType.Home)
                }
            )
        )
    )

    fun setActions(type: WidgetsScreenType){
        icons = Use[type]!!.plus(Navigation[type]!!)
    }

}