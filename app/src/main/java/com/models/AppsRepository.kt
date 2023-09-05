package com.models

import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    fun getAllAppsByName(): Flow<List<App>>

    fun getAppsByTimesOpened(): Flow<List<App>>

    suspend fun insertItem(item: App)

    suspend fun deleteItem(item: App)

    suspend fun updateItem(item: App)
}