package com.zenworklauncher.widgets.schedule.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zenworklauncher.widgets.schedule.model.Task

@Composable
fun TasksList(
    tasks : List<Task>,
    delete: (Task) -> Unit,
    complete: (Task) -> Unit
){
    LazyColumn(content = {
        items(tasks.size){
            TaskItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                task = tasks[it],
                delete = delete,
                complete = complete
            )
        }
    })
}