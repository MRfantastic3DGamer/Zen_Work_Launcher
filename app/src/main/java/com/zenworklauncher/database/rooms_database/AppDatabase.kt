package com.zenworklauncher.database.rooms_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zenworklauncher.model.GroupDataEntity

@Database(entities = [GroupDataEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDataDao(): GroupDataDao
}