package com.zenworklauncher.widgets.schedule.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.zenworklauncher.widgets.schedule.TasksViewModel
import com.zenworklauncher.widgets.schedule.data.TaskRepository
import com.zenworklauncher.widgets.schedule.data.source.local.TasksDatabase
import com.zenworklauncher.widgets.schedule.presentation.components.TaskEdit
import com.zenworklauncher.widgets.schedule.presentation.components.TasksList

@Composable
fun TasksWidget(){
    val db = TasksDatabase.getDatabase(LocalContext.current)
    val viewModel by remember { mutableStateOf(TasksViewModel(taskRepository = TaskRepository(db.taskDao())))}

    var showNewTaskEditor by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(viewModel.getAllTasks()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    showNewTaskEditor = false
                    tasks = viewModel.getAllTasks()
                }
            }
    )
    {
        Column {
            TasksList(
                tasks = tasks,
                delete = { t ->
                    viewModel.deleteTask(t)
                    tasks = viewModel.getAllTasks()
                },
                complete = {t ->
                    viewModel.updateTask(t.copy(completed = !t.completed))
                    tasks = viewModel.getAllTasks()
                }
            )
            IconButton(onClick = { showNewTaskEditor = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add task")
            }
        }

        TaskEdit(
            res = {
                viewModel.addTask(task = it)
                tasks = viewModel.getAllTasks()
                showNewTaskEditor = false
                  },
            visible = showNewTaskEditor
        )
    }

}