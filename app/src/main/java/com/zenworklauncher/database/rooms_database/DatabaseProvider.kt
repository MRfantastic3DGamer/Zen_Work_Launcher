package com.zenworklauncher.database.rooms_database

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.AppUIData
import com.zenworklauncher.model.AppUsageDataEntity
import com.zenworklauncher.model.AppsDataResult
import com.zenworklauncher.model.DatabaseState
import com.zenworklauncher.model.GroupDataEntity
import com.zenworklauncher.model.GroupsDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DatabaseProvider {

    private var appsDatabase: AppDatabase? = null

    private var allGroups               = listOf<GroupDataEntity>()
    private var originalNames           = listOf<String>()
    private var allAppUIData            = listOf<AppUIData>()
    private var allAppsUsageData        = listOf<AppUsageDataEntity>()

    private var packageToAppDataMap     = mapOf<String, Int>()
    private var firstLetterToAppDataMap = mapOf<String, List<Int>>()
    private var groupNameToAppDataMap   = mapOf<String, List<Int>>()
    private var packageToAppUsageDataMap= mapOf<String, Int>()

    private var _groupDataState         = MutableLiveData(DatabaseState.Uninitialized)
    private var _appsDataState          = MutableLiveData(DatabaseState.Uninitialized)

    private var regenerateAppUIData     = false

    private val groupDataState:LiveData<DatabaseState>
        get() {
            return _groupDataState
        }
    private val appsDataState:LiveData<DatabaseState>
        get() {
            return _appsDataState
        }

    private val groupsResult   = MutableLiveData<GroupsDataResult>()
    private val appsResult     = MutableLiveData<AppsDataResult>()

    init {
        groupDataState.observeForever {  state ->
            groupsResult.value = GroupsDataResult(
                resultState = state, allGroups   = allGroups)
        }
        appsDataState.observeForever { state ->
            if (regenerateAppUIData)
                renameAppsUIData()
            appsResult.value = AppsDataResult(
                resultState = state,
                allAppsData = allAppUIData,
                packageNameToAppDataMap = packageToAppDataMap,
                firstLetterToAppDataMap = firstLetterToAppDataMap,
                groupNameToAppDataMap = groupNameToAppDataMap,
                allAppsUsageData = allAppsUsageData,
                packageToAppUsageDataMap = packageToAppUsageDataMap
            )
        }
    }

    // region public getters

    val GetGroupResult: LiveData<GroupsDataResult>
        get () {
            return groupsResult
        }

    val GetAppsResult: LiveData<AppsDataResult>
        get (){
            return appsResult
        }


    // endregion

    fun initialize (packageManager: PackageManager, context: Context){
        _groupDataState.value       = DatabaseState.Uninitialized
        _appsDataState.value        = DatabaseState.Uninitialized
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

            getAllInstalledAppsData(packageManager)
            getPackageNameToAppDataMap()
            getFirstAlphabetToAppsDataMap()

            allAppsUsageData = getDatabase(context).appUsageDataDao().getAllApps()
            getMissingAppUsageData()
            deleteExtraAppUsageData()
            getPackageToAppUsageDataMap()

            getGroupToAppsDataMap()

            renameAppsUIData()

            withContext(Dispatchers.Main) {
                _groupDataState.value = DatabaseState.Ready
                _appsDataState.value = DatabaseState.Ready
            }
            println("Database ready")
        }
    }

    //region groups

    fun AddGroup (context: Context, newGroup: GroupDataEntity){
        _groupDataState.value = DatabaseState.Updating
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).groupDataDao().insertGroup(newGroup)
            allGroups = getDatabase(context).groupDataDao().getAllGroups()
            withContext(Dispatchers.Main) {
                _groupDataState.value = DatabaseState.Ready
            }
        }
    }
    fun DeleteGroup (context: Context, group: GroupDataEntity){
        _groupDataState.value = DatabaseState.Updating
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).groupDataDao().deleteGroup(group)
            allGroups = getDatabase(context).groupDataDao().getAllGroups()
            withContext(Dispatchers.Main) {
                _groupDataState.value = DatabaseState.Ready
            }
        }
    }

    // endregion

    // region app data

    private fun getAllInstalledAppsData (packageManager: PackageManager){
        val main = Intent(Intent.ACTION_MAIN, null)
        main.addCategory(Intent.CATEGORY_LAUNCHER)
        val appsL = packageManager.queryIntentActivities(main, 0)

        val installedApps: MutableList<AppUIData> = ArrayList()
        val names = mutableListOf<String>()

        for (app in appsL)
        {
            val name = app.loadLabel(packageManager) as String
            names.add(name)
            val saveApp = AppUIData(
                name,
                app.activityInfo.packageName,
                DrawablePainter(app.loadIcon(packageManager)),
                packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)
            )
            installedApps.add(saveApp)
        }
        installedApps.sortBy { it.name }
        allAppUIData = installedApps.toList()
        originalNames = names
    }

    private fun getPackageNameToAppDataMap (){packageToAppDataMap = allAppUIData.mapIndexed  { i, it -> it.packageName to i }.toMap() }

    private fun getFirstAlphabetToAppsDataMap (){
        val map = mutableMapOf<String, MutableList<Int>>()
        allAppUIData.forEachIndexed { i, it ->
            val key = if (it.Name.first().isLetter()) it.Name.first().uppercase() else ".."
            if (!map.containsKey(key)) map[key] = mutableListOf()
            map[key]?.add(i)
        }
        firstLetterToAppDataMap = map
    }

    private fun getGroupToAppsDataMap (){
        val map = mutableMapOf<String, MutableList<Int>>()
        allAppUIData.forEachIndexed{i, app ->
            if (!packageToAppUsageDataMap.containsKey(app.packageName))
                println("usage data for " + app.packageName + " is not available")
            else{
                val usage = packageToAppUsageDataMap[app.packageName]!!
                val appGroupName = allAppsUsageData[usage].group
                if (allGroups.any { it.name == appGroupName }){
                    if (!map.containsKey(appGroupName)) map[appGroupName] = mutableListOf()
                    map[appGroupName]?.add(i)
                }
            }
        }
        groupNameToAppDataMap = map
    }

    private fun renameAppsUIData (){
        val list = mutableListOf<AppUIData>()
        allAppUIData.forEachIndexed{ i, it ->
            list.add(it.copy(Name = allAppsUsageData[i].name))
        }
        allAppUIData = list
    }

    // endregion

    // region app usage data
    fun AddAppUsageData (context: Context, newAppUsageData: AppUsageDataEntity){
        _appsDataState.value = DatabaseState.Updating
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).appUsageDataDao().insertApp(newAppUsageData)
            getAllAppUsageData(context)
            withContext(Dispatchers.Main) {
                regenerateAppUIData = true
                _appsDataState.value = DatabaseState.Ready
            }
        }
    }
    fun DeleteAppUsageData (context: Context, appUsageData: AppUsageDataEntity){
        _appsDataState.value = DatabaseState.Updating
        GlobalScope.launch (Dispatchers.IO){
            getDatabase(context).appUsageDataDao().deleteApp(appUsageData)
            getAllAppUsageData(context)
            withContext(Dispatchers.Main) {
                _appsDataState.value = DatabaseState.Ready
            }
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
        allAppUIData.forEach{ app ->
            if (!(allAppsUsageData.any { it.`package` == app.packageName })){
                newAppUsageData.add(AppUsageDataEntity(`package` = app.packageName, name = app.Name))
            }
        }
        allAppsUsageData = newAppUsageData.toList()
    }

    private fun deleteExtraAppUsageData (){
        val newAppUsageData = allAppsUsageData.toMutableList()
        val toDelete = mutableListOf<AppUsageDataEntity>()
        allAppsUsageData.forEach { data ->
            if (!allAppUIData.any{app -> app.packageName == data.`package`})
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