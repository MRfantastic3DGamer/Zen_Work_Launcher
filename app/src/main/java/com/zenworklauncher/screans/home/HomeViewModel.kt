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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.lifecycle.ViewModel
import com.dhruv.quick_apps.QuickAppsViewModel
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.database.preffs_database.SettingsValues.AppsView.AppsViewKeys
import com.zenworklauncher.database.rooms_database.DatabaseProvider
import com.zenworklauncher.model.AppData
import com.zenworklauncher.model.DatabaseState

class HomeViewModel(
    val packageManager: PackageManager,
    var appWidgetHost: AppWidgetHost,
    var appWidgetManager: AppWidgetManager,
    val context: Context
) : ViewModel() {

    var searching               by mutableStateOf(false)
    var appSearchQuery          by mutableStateOf("")
    var searchedAppsData        by mutableStateOf(listOf<AppData>())
    var allAppsData             by mutableStateOf(listOf<AppData>())

    val appWidgetHostViewsIDs = mutableStateListOf<Int>()

    var quickAppsViewModel by mutableStateOf (QuickAppsViewModel(
        onAlphabetSelectionChange = {alphabet, haptic -> onAlphabetSelectionChange(alphabet, haptic)},
        onAppSelectionChange = {action, haptic -> onAppSelectionChange(action, haptic) },
        rowHeight = SettingsValues.getFloat(AppsViewKeys.iconRowSeparation).toDouble(),
        distanceBetweenIcons = SettingsValues.getFloat(AppsViewKeys.iconSeparation).toDouble(),
        sidePadding = 150f,
    ))

    init {
        DatabaseProvider.GetAppsResult.observeForever { res ->
            if (res.resultState == DatabaseState.Ready){
                allAppsData = res.allAppsData
                quickAppsViewModel.onActionSelect = { context.startActivity(allAppsData[it].launchIntent) }
                quickAppsViewModel.firstCharToActionsMap = res.firstLetterToAppDataMap
                quickAppsViewModel.groupNameToActionsMap = res.groupNameToAppDataMap
            }
        }
    }



    // region apps data
    private fun onAlphabetSelectionChange (alphabet: String, haptic: HapticFeedback){
        if(!SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled))
            return
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }

    private fun onAppSelectionChange (action: Int?, haptic: HapticFeedback){
        if(!SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled))
            return
        if(action == null) return
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }


    fun onRebuildIconCoordinates(){
        quickAppsViewModel.rowHeight = SettingsValues.getFloat(AppsViewKeys.iconRowSeparation).toDouble()
        quickAppsViewModel.distanceBetweenIcons = SettingsValues.getFloat(AppsViewKeys.iconSeparation).toDouble()
    }
    // endregion

    // region widgets
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

    // endregion

    fun onTriggerPositioned (layoutCoordinates: LayoutCoordinates){
        DatabaseProvider.initialize(packageManager, context)
        quickAppsViewModel.onTriggerGloballyPositioned(layoutCoordinates)
    }

    // region dragging
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
    // endregion

    // region searching
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
    // endregion

}