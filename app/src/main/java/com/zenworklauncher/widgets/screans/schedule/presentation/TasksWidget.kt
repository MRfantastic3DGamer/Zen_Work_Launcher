package com.zenworklauncher.widgets.screans.schedule.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.zenworklauncher.widgets.screans.schedule.ScheduleViewModel
import com.zenworklauncher.widgets.screans.schedule.presentation.components.DayTimeVisual
import com.zenworklauncher.widgets.screans.schedule.presentation.components.TaskEditor
import com.zenworklauncher.widgets.screans.schedule.presentation.components.TaskItem
import com.zenworklauncher.widgets.screans.schedule.presentation.components.TasksLayout

@Composable
fun ScheduleWidget(
    viewModel: ScheduleViewModel
){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            TasksLayout(
                tasks = viewModel.tasksUiState.items,
                content = {
                    repeat(viewModel.tasksUiState.items.size){ i ->
                        TaskItem(
                            task = viewModel.tasksUiState.items[i],
                            expand = viewModel.tasksUiState.currentFocusTask == i,
                            onTap = {
                                viewModel.deleteTask(viewModel.tasksUiState.items[i])
                            },
                            onHold = {
                                viewModel.setCurrentEditTask(viewModel.tasksUiState.items[i])
                            }
                        )
                    }
                }
            )
            DayTimeVisual(
                screenHeight = screenHeight
            )
        }
        TaskEditor(
            viewModel = viewModel
        )
    }
}
