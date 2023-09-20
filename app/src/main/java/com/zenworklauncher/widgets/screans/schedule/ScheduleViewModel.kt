package com.zenworklauncher.widgets.screans.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zenworklauncher.widgets.screans.schedule.data.TaskRepository
import com.zenworklauncher.widgets.screans.schedule.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ScheduleUiState(
    val items: List<Task> = listOf<Task>(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val currentEditedTask: Task = Task(name = "", description = "", notificationTime = "0:0", alarmTime = "0:0", completed = false),
    val currentlyEditing: Boolean = false,
    val currentFocusTask: Int = 0
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    var tasksUiState by mutableStateOf(
        ScheduleUiState(
            items = taskRepository.allTasks,
        )
    )

    fun getAllTasks(){
        taskRepository.getAllTasks()
        tasksUiState = tasksUiState.copy(
            items = taskRepository.allTasks,
            currentlyEditing = false
        )
    }

    fun getAllTasksByNotificationTime(){
        taskRepository.getTasksByNotificationTime()
        tasksUiState = tasksUiState.copy(
            items = taskRepository.sortedTasks
        )
    }

    fun getAllTasksByAlarmTime(){
        taskRepository.getTasksByAlarmTime()
        tasksUiState = tasksUiState.copy(
            items = taskRepository.sortedTasks
        )
    }

    fun addTask(task: Task){
        taskRepository.addTask(task = task)
        getAllTasks()
    }

    fun updateTask(task: Task){
        taskRepository.updateTask(task = task)
        getAllTasks()
    }

    fun upsertTask(task: Task){
        taskRepository.upsertTask(task = task)
        getAllTasks()
    }

    fun deleteTask(task: Task){
        taskRepository.deleteTask(task = task)
        getAllTasks()
    }

    fun setCurrentEditTask(task: Task){
        tasksUiState = tasksUiState.copy(
            currentEditedTask = task,
            currentlyEditing = true,
        )
    }

    fun endEditingTask(){
        tasksUiState = tasksUiState.copy(
            currentEditedTask = Task(name = "", description = "", notificationTime = "0:0", alarmTime = "0:0", completed = false),
            currentlyEditing = false,
        )
        getAllTasks()
    }

    fun refresh(){
        getAllTasksByAlarmTime()
        val currentTime = LocalTime.now()

        for (i in 0 until tasksUiState.items.size) {
            val taskTime = LocalTime.parse(tasksUiState.items[i].notificationTime, DateTimeFormatter.ofPattern("HH:mm"))

            if (taskTime.isAfter(currentTime)) {
                tasksUiState = tasksUiState.copy(
                    currentFocusTask = i,
                )
                return
            }
        }
        tasksUiState = tasksUiState.copy(
            currentFocusTask = 0,
        )
    }
}