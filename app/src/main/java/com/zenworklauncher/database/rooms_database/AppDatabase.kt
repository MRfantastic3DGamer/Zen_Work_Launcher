package com.zenworklauncher.database.rooms_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zenworklauncher.model.AppUsageDataEntity
import com.zenworklauncher.model.GroupDataEntity

@Database(entities = [GroupDataEntity::class, AppUsageDataEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDataDao(): GroupDataDao
    abstract fun appUsageDataDao(): AppUsageDataDao
}