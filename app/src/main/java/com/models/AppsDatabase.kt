package com.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [App::class], version = 1, exportSchema = false)
abstract class AppsDatabase : RoomDatabase() {
    abstract fun appsDao(): AppsDao

    companion object {
        @Volatile
        private var Instance: AppsDatabase? = null

        fun getDatabase(context: Context): AppsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppsDatabase::class.java, "apps_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}