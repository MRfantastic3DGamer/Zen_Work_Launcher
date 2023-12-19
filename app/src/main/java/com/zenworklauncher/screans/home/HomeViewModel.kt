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
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.home.model.AppData
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
        firstCharToActionsMap = getAllAppsFromPackageManager(),
        rowHeight = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconRowSeparation).toDouble(),
        distanceBetweenIcons = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconSeparation).toDouble(),
        sidePadding = 150f,
    ))

    private fun getAllAppsFromPackageManager() : Map<String, List<AppData>>{
        allAppsData = getMyAllAppsData()
        val installedApps: MutableList<AppData> = allAppsData.toMutableList()
        val res = mutableMapOf<String, MutableList<AppData>>()
        installedApps.sortBy { t->
            t.Name
        }
        installedApps.forEach { action ->
            if(!action.Name[0].isLetter()){
                if(res["@"] == null)
                    res["@"] = mutableListOf()
                res["@"]!!.add(action)
            }
            else {
                if (res[action.Name[0].uppercase()] == null)
                    res[action.Name[0].uppercase()] = mutableListOf()
                res[action.Name[0].uppercase()]!!.add(action)
            }
        }
        return res.toMap()
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
        return installedApps.toList()
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

    fun onDragStop (context: Context){
        quickAppsViewModel.onDragStop(context)
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
}