package com.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(app: App)

    @Update
    suspend fun update(app: App)

    @Delete
    suspend fun delete(app: App)

    @Query("SELECT * from apps ORDER BY name ASC")
    fun getAllItemsOrderedByName(): Flow<List<App>>

    @Query("SELECT * from apps ORDER BY noOfTimesOpened ASC")
    fun getAllItemsOrderedByNoOfTimesOpened(): Flow<List<App>>
}