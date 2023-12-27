package com.zenworklauncher.database.rooms_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zenworklauncher.model.GroupDataEntity

@Dao
interface GroupDataDao {
    @Upsert
    suspend fun insertGroup(groupDataEntity: GroupDataEntity)
    @Delete
    suspend fun deleteGroup(groupDataEntity: GroupDataEntity)

    @Query("SELECT * FROM groups")
    suspend fun getAllGroups(): List<GroupDataEntity>
}