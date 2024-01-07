package com.zenworklauncher.screans.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.database.rooms_database.DatabaseProvider
import com.zenworklauncher.model.AppUsageDataEntity
import com.zenworklauncher.model.DatabaseState
import com.zenworklauncher.model.GroupDataEntity
import com.zenworklauncher.screans.home.HomeViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsViewModel(
    val homeViewModel: HomeViewModel
): ViewModel() {

    var height by mutableStateOf(0.dp)
    var width by mutableStateOf(0.dp)
    var labelWidth by mutableStateOf(0.dp)
    var labelSize by mutableStateOf(0f)
    var iconSize by mutableStateOf(0.dp)
    var iconBorderSize by mutableStateOf(0.dp)
    var selectedIconBorderSize by mutableStateOf(0.dp)

    var foldersPageState by mutableStateOf(FoldersPageState(allGroups = listOf()))
    var appsSettingsState by mutableStateOf(AppsSettingsState(listOf(), listOf(), listOf(), listOf()))

    var currentlyBeingEdited = mutableStateOf<SettingsValues.AppsView.AppsViewKeys?>(null)
    var shouldRebuildIconPositions = false

    init {
        DatabaseProvider.GetGroupResult.observeForever { state ->
            if (state.resultState == DatabaseState.Ready){
                foldersPageState = foldersPageState.copy(
                    allGroups = state.allGroups
                )
                appsSettingsState = appsSettingsState.copy(
                    allGroups = state.allGroups.map { it.name }
                )
            }
        }
        DatabaseProvider.GetAppsResult.observeForever { state ->
            if (state.resultState == DatabaseState.Ready){
                val apps = state.allAppsData.toMutableList()
                apps.sortBy { it.Name }
                val appsUsageData = mutableListOf<AppUsageDataEntity>()
                apps.forEach {
                    val i = state.packageToAppUsageDataMap[it.packageName]
                    if (i != null){
                        appsUsageData.add(state.allAppsUsageData[i])
                    }
                }
                appsSettingsState = appsSettingsState.copy(
                    allAppsUsageData = appsUsageData,
                    icons = apps.map { it.painter }.toList(),
                    names = apps.map { it.Name }.toList(),
                )
            }
        }
    }

    fun UpdateValues(){

        height = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.height)
        width = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.width)
        labelWidth = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.labelWidth)
        labelSize = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.labelSize)
        iconSize = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.iconSize)
        iconBorderSize = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.iconBorderSize)
        selectedIconBorderSize = SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.selectedIconBorderSize)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun save(context: Context) {
        if(shouldRebuildIconPositions){
            println("shouldRebuildIconPositions")
            homeViewModel.onRebuildIconCoordinates()
            homeViewModel.quickAppsViewModel.markDirty()
            GlobalScope.launch(Dispatchers.IO) {
                homeViewModel.quickAppsViewModel.handleIconsPositioningCalculations()
                println("done")
            }
            SettingsValues.saveData(context)
        }
    }

    fun addGroup(context: Context, group: GroupDataEntity){
        DatabaseProvider.AddGroup(context, group)
    }

    fun deleteGroup(context: Context, group: GroupDataEntity){
        DatabaseProvider.DeleteGroup(context, group)
    }
}