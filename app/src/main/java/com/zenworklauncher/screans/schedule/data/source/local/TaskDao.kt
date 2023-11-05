package com.zenworklauncher.screans.schedule.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.zenworklauncher.screans.schedule.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    suspend fun getAllTask() : List<Task>
    @Query("SELECT * FROM task ORDER BY notificationTime")
    suspend fun getTasksByNotificationTime() : List<Task>
    @Query("SELECT * FROM task ORDER BY alarmTime")
    suspend fun getTasksByAlarmTime() : List<Task>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : Task)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: Task)
    @Upsert(entity = Task::class)
    suspend fun upsertTask(task: Task)
    @Delete
    suspend fun deleteTask(task : Task)
}