package com.zenworklauncher.widgets.screans.schedule.data

import androidx.lifecycle.MutableLiveData
import com.zenworklauncher.widgets.screans.schedule.data.source.local.TaskDao
import com.zenworklauncher.widgets.screans.schedule.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskRepository(private val tasksDao: TaskDao) {

    var allTasks = listOf<Task>()
    var sortedTasks = listOf<Task>()
    val selectedTask = MutableLiveData<Task>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addTask(task: Task){
        coroutineScope.launch(Dispatchers.IO) {
            tasksDao.insertTask(task)
        }
    }

    fun updateTask(task: Task){
        coroutineScope.launch(Dispatchers.IO) {
            tasksDao.updateTask(task)
        }
    }

    fun upsertTask(task: Task){
        coroutineScope.launch(Dispatchers.IO) {
            tasksDao.upsertTask(task)
        }
    }

    fun deleteTask(task: Task){
        coroutineScope.launch(Dispatchers.IO) {
            tasksDao.deleteTask(task)
        }
    }

    fun getAllTasks() {
        coroutineScope.launch(Dispatchers.IO) {
            allTasks = tasksDao.getAllTask()
        }
    }

    fun getTasksByNotificationTime(){
        coroutineScope.launch(Dispatchers.IO) {
            sortedTasks = tasksDao.getTasksByNotificationTime()
        }
    }

    fun getTasksByAlarmTime(){
        coroutineScope.launch(Dispatchers.IO) {
            sortedTasks = tasksDao.getTasksByAlarmTime()
        }
    }
}