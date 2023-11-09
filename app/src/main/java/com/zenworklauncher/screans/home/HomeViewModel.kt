package com.zenworklauncher.screans.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dhruv.quick_apps.QuickAppsViewModel
import com.dhruv.radial_quick_actions.RadialQuickActionViewModel
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.home.model.AppData
import com.zenworklauncher.screans.home.presentation.HomeQuickAction

class HomeViewModel(
    val packageManager: PackageManager,
    val context: Context
) : ViewModel() {

    val radialViewModel : RadialQuickActionViewModel by mutableStateOf (RadialQuickActionViewModel(
        listOf(
            HomeQuickAction("0",{}),
            HomeQuickAction("1",{}),
            HomeQuickAction("2",{}),
            HomeQuickAction("3",{}),
            HomeQuickAction("4",{}),
        )
    ))

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
        actionsMap = getAllAppsFromPackageManager(),
        rowHeight =
            SettingsValues.AppsView.savedData.cacheValues[SettingsValues.AppsView.AppsViewKeys.iconRowSeparation]?.toDouble()
            ?: 150.0,
        distanceBetweenIcons =
            SettingsValues.AppsView.savedData.cacheValues[SettingsValues.AppsView.AppsViewKeys.iconSeparation]?.toDouble()
            ?: 180.0,
        sidePadding = 150f,
    ))

    private fun getAllAppsFromPackageManager() : Map<String, List<AppData>>{
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
}


