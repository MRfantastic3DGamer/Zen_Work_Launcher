package com.zenworklauncher.database.rooms_database

import android.content.Context
import androidx.room.Room
import com.zenworklauncher.model.GroupDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DatabaseProvider {

    private var database: AppDatabase? = null

    var allGroups = listOf<GroupDataEntity>()


    fun initialize (context: Context){
        getAllGroups(context)
    }

    fun getGroupsDatabase (context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration().build()
            database = instance
            instance
        }
    }

    fun getAllGroups (context: Context) {
        GlobalScope.launch (Dispatchers.IO){
            allGroups = getGroupsDatabase(context).groupDataDao().getAllGroups()
        }
    }
    fun addGroup (context: Context, oldGroup: GroupDataEntity? = null, newGroup: GroupDataEntity){
        val prevList = allGroups.toMutableList()

        if (oldGroup != null)
            prevList.remove(oldGroup)

        prevList.add(newGroup)
        allGroups = prevList
        GlobalScope.launch (Dispatchers.IO){
            getGroupsDatabase(context).groupDataDao().insertGroup(newGroup)
        }
        println("all groups are : $allGroups")
    }
    fun deleteGroup (context: Context, group: GroupDataEntity){
        val prev = allGroups.toMutableList()
        prev.remove(group)
        allGroups = prev.toList()
        GlobalScope.launch (Dispatchers.IO){
            getGroupsDatabase(context).groupDataDao().deleteGroup(group)
            allGroups = getGroupsDatabase(context).groupDataDao().getAllGroups()
        }
    }
}