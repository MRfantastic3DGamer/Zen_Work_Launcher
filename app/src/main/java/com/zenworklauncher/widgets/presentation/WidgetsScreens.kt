package com.zenworklauncher.widgets.presentation

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.zenworklauncher.widgets.quick_action.QuickActionViewModel
import com.zenworklauncher.widgets.screans.schedule.ScheduleViewModel
import com.zenworklauncher.widgets.screans.schedule.data.TaskRepository
import com.zenworklauncher.widgets.screans.schedule.data.source.local.TasksDatabase
import com.zenworklauncher.widgets.view.WidgetsScreenViewModel

@Composable
fun WidgetsScreens() {

    val context = LocalContext.current

    val viewModel : WidgetsScreenViewModel by remember { mutableStateOf(WidgetsScreenViewModel()) }
//    val homeViewModel : HomeViewModel by remember { mutableStateOf(HomeViewModel()) }
    val scheduleViewModel : ScheduleViewModel by remember { mutableStateOf(ScheduleViewModel(
        TaskRepository(TasksDatabase.getDatabase(context).taskDao())
    )) }
    val quickActionViewModel: QuickActionViewModel by remember {
        mutableStateOf(QuickActionViewModel(viewModel,scheduleViewModel))
    }

    val currentIntOffset by animateIntOffsetAsState(
        targetValue = viewModel.currentScreenOffset,
        label = "widgets screen offset"
    )


    LaunchedEffect(key1 = viewModel.currentScreen, block = {
        quickActionViewModel.setActions(viewModel.currentScreen)
    })



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
//        QuickActionZone(
//            modifier = Modifier
//                .offset { IntOffset(300, 1200) }
//                .size(250.dp, 300.dp)
////                .background(Color.Red)
//            ,
//            quickActionViewModel = quickActionViewModel
//        ){
//            AnimatedVisibility(visible = viewModel.screenVisibility[WidgetsScreenType.Home] as Boolean) {
//                HomeScreen(viewModel = homeViewModel)
//            }
//            AnimatedVisibility(visible = viewModel.screenVisibility[WidgetsScreenType.Schedule] as Boolean) {
//                ScheduleWidget(viewModel = scheduleViewModel)
//            }
//        }
    }
}