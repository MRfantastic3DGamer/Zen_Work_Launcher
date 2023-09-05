package com.models

import kotlinx.coroutines.flow.Flow

class OfflineAppsRepository(private val appsDao: AppsDao) : AppsRepository{

    override fun getAllAppsByName(): Flow<List<App>> = appsDao.getAllItemsOrderedByName()

    override fun getAppsByTimesOpened(): Flow<List<App>> = appsDao.getAllItemsOrderedByNoOfTimesOpened()

    override suspend fun insertItem(item: App) = appsDao.insert(item)

    override suspend fun deleteItem(item: App) = appsDao.delete(item)

    override suspend fun updateItem(item: App) = appsDao.update(item)
}