package com.zenworklauncher.screans.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.lifecycle.ViewModel
import com.dhruv.quick_apps.QuickAppsViewModel
import com.dhruv.radial_quick_actions.RadialQuickActionViewModel
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.screans.home.model.AppData
import com.zenworklauncher.screans.home.model.AppRefData
import com.zenworklauncher.screans.home.model.HomeFocusEnum
import com.zenworklauncher.screans.home.presentation.HomeQuickAction

class HomeViewModel(
    val packageManager: PackageManager,
    var appWidgetHost: AppWidgetHost,
    var appWidgetManager: AppWidgetManager,
    val context: Context
) : ViewModel() {

    var homeFocusEnum           by mutableStateOf(HomeFocusEnum.Home)
    var allAppsData             by mutableStateOf(listOf<AppData>())
    var refAppsData             by mutableStateOf(listOf<AppRefData>())
    var searching               by  mutableStateOf(false)
    var appSearchQuery          by mutableStateOf("")
    var searchedAppsData        by mutableStateOf(listOf<AppData>())

    val appWidgetHostViewsIDs = mutableStateListOf<Int>()

    val radialViewModel : RadialQuickActionViewModel by mutableStateOf (RadialQuickActionViewModel(listOf(
            HomeQuickAction("0",{}),
            HomeQuickAction("1",{}),
            HomeQuickAction("2",{}),
            HomeQuickAction("3",{}),
            HomeQuickAction("4",{}),
        )))

    val quickAppsViewModel by mutableStateOf (QuickAppsViewModel(
        onAlphabetSelectionChange = {alphabet, haptic ->
            if(!SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled))
                return@QuickAppsViewModel
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        },
        onAppSelectionChange = {action, haptic ->
            if(!SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled))
                return@QuickAppsViewModel
            if(action == null) return@QuickAppsViewModel
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        },
        onActionSelect = {
            context.startActivity(allAppsData[it].launchIntent)
        },
        firstCharToActionsMap = getAllAppsFromPackageManager(),
        groupNameToActionsMap = getAppGroupsFromStorage(),
        rowHeight = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconRowSeparation).toDouble(),
        distanceBetweenIcons = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconSeparation).toDouble(),
        sidePadding = 150f,
    ))

    private fun getAllAppsFromPackageManager() : Map<String, MutableList<Int>> {
        allAppsData = getMyAllAppsData()
        refAppsData = generateAppsRefData()


        val installedApps: MutableList<AppRefData> = refAppsData.toMutableList()
        val res = mutableMapOf<String, MutableList<Int>>()
        installedApps.forEachIndexed { index, action ->
            if (!action.name[0].isLetter()) {
                if (res["@"] == null)
                    res["@"] = mutableListOf()
                res["@"]!!.add(index)
            } else {
                if (res[action.name[0].uppercase()] == null)
                    res[action.name[0].uppercase()] = mutableListOf()
                res[action.name[0].uppercase()]!!.add(index)
            }
        }
        return res.toMap()
    }

    private fun getAppGroupsFromStorage() : Map<String, List<Int>>{
        val appsMap = mutableMapOf<String, List<Int>>()
        // TODO: Get app groups from storage
        return appsMap
    }

    private fun getMyAllAppsData(): List<AppData> {
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
        return installedApps.toList()
    }

    private fun generateAppsRefData(): List<AppRefData> {
        val refApps: MutableList<AppRefData> = ArrayList()

        for (i in allAppsData.indices)
            refApps.add(AppRefData(allAppsData[i].Name, i))

        return refApps
    }

    fun onRebuildIconCoordinates(){
        quickAppsViewModel.rowHeight = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconRowSeparation).toDouble()
        quickAppsViewModel.distanceBetweenIcons = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconSeparation).toDouble()
    }

    fun removeWidget(hostView: AppWidgetHostView) {
        appWidgetHost.deleteAppWidgetId(hostView.appWidgetId)
        appWidgetHostViewsIDs.remove(hostView.appWidgetId)
    }

    fun createWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo: AppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
        val hostView: AppWidgetHostView =
            appWidgetHost.createView(context, appWidgetId, appWidgetInfo)
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        hostView.updateAppWidgetSize(options, 600, appWidgetInfo.minHeight, 600, appWidgetInfo.maxResizeHeight)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        appWidgetHostViewsIDs.add(hostView.appWidgetId)
    }

    fun onTriggerPositioned (layoutCoordinates: LayoutCoordinates){
        quickAppsViewModel.onTriggerGloballyPositioned(layoutCoordinates)
    }

    fun onDragStart (offset: Offset){
        searching = false
        quickAppsViewModel.onDragStart(offset)
        stopAppSearching()
    }

    fun onDrag (change: Offset){
        quickAppsViewModel.onDrag(change)
    }

    fun onDragStop (){
        quickAppsViewModel.onDragStop()
    }

    fun startAppSearching (){
        searching = true
    }

    fun appSearch (v: String){
        searching = true
        appSearchQuery = v
        // TODO: implement Trie for searching
        searchedAppsData = allAppsData.filter {
            it.Name         .contains(v, ignoreCase = true) or
            it.packageName  .contains(v, ignoreCase = true)
        }
        println(searchedAppsData.map { it.Name }.toList())
    }

    fun stopAppSearching (){
        searching = false
    }

    companion object {
        var packageToData: Map<String, Int> = mapOf()
    }
}