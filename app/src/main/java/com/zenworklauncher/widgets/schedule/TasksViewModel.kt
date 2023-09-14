package com.zenworklauncher.widgets.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zenworklauncher.widgets.schedule.data.TaskRepository
import com.zenworklauncher.widgets.schedule.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class TasksUiState(
    val items: List<Task> = listOf<Task>(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val showTaskEditor: Task? = null,

    )

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    var tasksUiState by mutableStateOf(TasksUiState(
        items = taskRepository.allTasks,
    ))

    fun getAllTasks(): List<Task> {
        taskRepository.getAllTasks()
        return taskRepository.allTasks
    }

    fun getAllTasksByNotificationTime(){
        return taskRepository.getTasksByNotificationTime()
    }

    fun getAllTasksByAlarmTime(){
        return taskRepository.getTasksByAlarmTime()
    }

    fun addTask(task: Task){
        taskRepository.addTask(task = task)
        getAllTasks()
    }

    fun updateTask(task: Task){
        taskRepository.updateTask(task = task)
        getAllTasks()
    }

    fun deleteTask(task: Task){
        taskRepository.deleteTask(task = task)
        getAllTasks()
    }

    fun setCurrentEditTask(task: Task){
        tasksUiState = tasksUiState.copy(
            showTaskEditor = task
        )
    }

    fun endEditingTask(){
        tasksUiState = tasksUiState.copy(
            showTaskEditor = null
        )
    }
}