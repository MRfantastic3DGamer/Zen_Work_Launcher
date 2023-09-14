package com.zenworklauncher.widgets.schedule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.zenworklauncher.widgets.schedule.model.Task

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    delete: (Task)->Unit,
    complete: (Task)->Unit,
){
    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 5.dp,
                    bottomStart = 5.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp
                )
            )
            .background(if(task.completed) Color.Green else Color.White)
            .pointerInput(Unit){
                detectTapGestures {
                    complete(task)
//                    delete(task)
                }
            }
    ){
        Row {
            Text(text = task.name)
        }
    }
}