package com.zenworklauncher.database.rooms_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zenworklauncher.model.AppUsageDataEntity

@Dao
interface AppUsageDataDao {

    @Upsert
    suspend fun insertApp(app: AppUsageDataEntity)
    @Delete
    suspend fun deleteApp(app: AppUsageDataEntity)

    @Query("SELECT * FROM app_usage")
    suspend fun getAllApps(): List<AppUsageDataEntity>
}