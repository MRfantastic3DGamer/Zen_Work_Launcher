package com.zenworklauncher.widgets.screans.schedule.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val name: String,
    val description: String,
    val notificationTime: String,
    val alarmTime: String,
    val completed: Boolean
)
