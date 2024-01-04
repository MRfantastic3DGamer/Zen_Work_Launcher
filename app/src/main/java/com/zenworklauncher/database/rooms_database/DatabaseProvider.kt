package com.zenworklauncher.database.rooms_database

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.AppData
import com.zenworklauncher.model.AppUsageDataEntity
import com.zenworklauncher.model.AppsDataResult
import com.zenworklauncher.model.AppsUsageDataResult
import com.zenworklauncher.model.DatabaseState
import com.zenworklauncher.model.GroupDataEntity
import com.zenworklauncher.model.GroupsDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DatabaseProvider {

    private var appsDatabase: AppDatabase? = null

    private var allGroups               = listOf<GroupDataEntity>()
    private var allAppData              = listOf<AppData>()
    private var allAppsUsageData        = listOf<AppUsageDataEntity>()

    private var packageToAppDataMap     = mapOf<String, Int>()
    private var firstLetterToAppDataMap = mapOf<String, List<Int>>()
    private var groupNameToAppDataMap   = mapOf<String, List<Int>>()
    private var packageToAppUsageDataMap= mapOf<String, Int>()

    private var _groupDataState         = MutableLiveData(DatabaseState.Uninitialized)
    private val groupDataState:LiveData<DatabaseState>
        get() {
            return _groupDataState
        }
    private var _appsDataState           = MutableLiveData(DatabaseState.Uninitialized)
    private val appsDataState:LiveData<DatabaseState>
        get() {
            return _appsDataState
        }
    private var _appsUsageDataState      = MutableLiveData(DatabaseState.Uninitialized)
    private val appsUsageDataState:LiveData<DatabaseState>
        get() {
            return _appsUsageDataState
        }

    init {
        groupDataState.observeForever {  state ->
            groupsResult.value = GroupsDataResult(
                resultState = state, allGroups   = allGroups)
        }
        appsDataState.observeForever { state ->
            appsResult.value = AppsDataResult(
                resultState = state,
                allAppsData = allAppData,
                packageNameToAppDataMap = packageToAppDataMap,
                firstLetterToAppDataMap = firstLetterToAppDataMap,
                groupNameToAppDataMap = groupNameToAppDataMap
            )
        }
        appsUsageDataState.observeForever { state ->
            appsUsageResult.value = AppsUsageDataResult(
                resultState = state,
                allAppsUsageData = allAppsUsageData,
                packageToAppUsageDataMap = packageToAppUsageDataMap
            )
        }
    }

    // region public getters

    private val groupsResult   = MutableLiveData<GroupsDataResult>()
    private val appsResult     = MutableLiveData<AppsDataResult>()
    private val appsUsageResult= MutableLiveData<AppsUsageDataResult>()

    val GetGroupResult: LiveData<GroupsDataResult>
        get () {
            return groupsResult
        }

    val GetAppsResult: LiveData<AppsDataResult>
        get (){
            return appsResult
        }

    val GetAppsUsageResult: LiveData<AppsUsageDataResult>
        get (){
            return appsUsageResult
        }


    // endregion

    fun initialize (packageManager: PackageManager, context: Context){
        _groupDataState.value       = DatabaseState.Uninitialized
        _appsDataState.value        = DatabaseState.Uninitialized
        _appsUsageDataState.value   = DatabaseState.Uninitialized
        getAllData(packageManager, context)
    }

    fun getDatabase (context: Context): AppDatabase {
        return appsDatabase ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration().build()
            appsDatabase = instance
            instance
        }
    }


    private fun getAllData (packageManager: PackageManager, context: Context) {
        GlobalScope.launch (Dispatchers.IO){
            allGroups = getDatabase(context).groupDataDao().getAllGroups()
            _groupDataState.value = DatabaseState.Ready
        }
        GlobalScope.launch (Dispatchers.IO){

            getAllInstalledAppsData(packageManager)
            getPackageNameToAppDataMap()
            getFirstAlphabetToAppsDataMap()

            allAppsUsageData = getDatabase(context).appUsageDataDao().getAllApps()
            getMissingAppUsageData()
            deleteExtraAppUsageData()
            getPackageToAppUsageDataMap()

            getGroupToAppsDataMap()

            _appsDataState.value = DatabaseState.Ready
            _appsUsageDataState.value = DatabaseState.Ready
        }
    }

    //region groups

    fun AddGroup (context: Context, oldGroup: GroupDataEntity? = null, newGroup: GroupDataEntity){
        val prevList = allGroups.toMutableList()

        if (oldGroup != null)
            prevList.remove(oldGroup)

        prevList.add(newGroup)
        allGroups = prevList
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).groupDataDao().insertGroup(newGroup)
        }
        println("all groups are : $allGroups")
    }
    fun DeleteGroup (context: Context, group: GroupDataEntity){
        val prev = allGroups.toMutableList()
        prev.remove(group)
        allGroups = prev.toList()
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).groupDataDao().deleteGroup(group)
            allGroups = getDatabase(context).groupDataDao().getAllGroups()
        }
    }

    // endregion

    // region app data

    private fun getAllInstalledAppsData (packageManager: PackageManager){
        val main = Intent(Intent.ACTION_MAIN, null)
        main.addCategory(Intent.CATEGORY_LAUNCHER)
        val appsL = packageManager.queryIntentActivities(main, 0)

        val installedApps: MutableList<AppData> = ArrayList()

        for (app in appsL)
        {
            val saveApp = AppData(
                app.loadLabel(packageManager) as String,
                app.activityInfo.packageName,
                DrawablePainter(app.loadIcon(packageManager)),
                packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)
            )
            installedApps.add(saveApp)
        }
        installedApps.sortBy { it.name }
        allAppData = installedApps.toList()
    }

    private fun getPackageNameToAppDataMap (){packageToAppDataMap = allAppData.mapIndexed  { i, it -> it.packageName to i }.toMap() }

    private fun getFirstAlphabetToAppsDataMap (){
        val map = mutableMapOf<String, MutableList<Int>>()
        allAppData.forEachIndexed { i, it ->
            val key = if (it.Name.first().isLetter()) it.Name.first().uppercase() else ".."
            if (!map.containsKey(key)) map[key] = mutableListOf()
            map[key]?.add(i)
        }
        firstLetterToAppDataMap = map
    }

    private fun getGroupToAppsDataMap (){
        val map = mutableMapOf<String, MutableList<Int>>()
        allAppData.forEachIndexed{i, app ->
            val appGroupName = allAppsUsageData[packageToAppUsageDataMap[app.packageName]!!].group
            if (appGroupName != null && allGroups.any { it.name == appGroupName }){
                if (!map.containsKey(appGroupName)) map[appGroupName] = mutableListOf()
                map[appGroupName]?.add(i)
            }
        }
        groupNameToAppDataMap = map
    }

    // endregion

    // region app usage data
    fun AddAppUsageData (context: Context, oldAppUsageData: AppUsageDataEntity? = null, newAppUsageData: AppUsageDataEntity){
        GlobalScope.launch (Dispatchers.IO){
            _appsUsageDataState.value = DatabaseState.Updating
            getDatabase(context).appUsageDataDao().insertApp(newAppUsageData)
            getAllAppUsageData(context)
            _appsUsageDataState.value = DatabaseState.Ready
        }
    }
    fun DeleteAppUsageData (context: Context, appUsageData: AppUsageDataEntity){

        GlobalScope.launch (Dispatchers.IO){
            _appsUsageDataState.value = DatabaseState.Updating
            getDatabase(context).appUsageDataDao().deleteApp(appUsageData)
            getAllAppUsageData(context)
            _appsUsageDataState.value = DatabaseState.Ready
        }
    }

    private fun getAllAppUsageData (context: Context) {
        GlobalScope.launch (Dispatchers.IO){
            allAppsUsageData = getDatabase(context).appUsageDataDao().getAllApps()
            getPackageToAppUsageDataMap()
        }
    }

    /**
     * create app usage data for new apps
     */
    private fun getMissingAppUsageData (){
        val newAppUsageData = allAppsUsageData.toMutableList()
        allAppData.forEach{ app ->
            if (!(allAppsUsageData.any { it.`package` == app.packageName })){
                newAppUsageData.add(AppUsageDataEntity(`package` = app.packageName))
            }
        }
        allAppsUsageData = newAppUsageData.toList()
    }

    private fun deleteExtraAppUsageData (){
        val newAppUsageData = allAppsUsageData.toMutableList()
        val toDelete = mutableListOf<AppUsageDataEntity>()
        allAppsUsageData.forEach { data ->
            if (!allAppData.any{app -> app.packageName == data.`package`})
                toDelete.add(data)
        }
        toDelete.forEach{data ->
            newAppUsageData.remove(data)
        }
        allAppsUsageData = newAppUsageData.toList()
    }

    /**
     * get app usage data from package name
     */
    private fun getPackageToAppUsageDataMap (){ packageToAppUsageDataMap = allAppsUsageData.mapIndexed { i, it -> it.`package` to i }.toMap() }

    // endregion

}