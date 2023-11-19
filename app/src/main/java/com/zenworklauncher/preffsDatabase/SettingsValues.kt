package com.zenworklauncher.preffsDatabase

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SettingsValues{

    enum class SettingsTabType{
        Main,
        AppsView,
        Folders,
    }

    sealed class Main{
        enum class MainKeys{
            HapticsEnabled,

        }
        object savedData {
            val defaultValues
                get() = mapOf<MainKeys,String>(
                    MainKeys.HapticsEnabled to "true"
                )

            val cacheValues = mutableMapOf<Main.MainKeys, String>()
        }

        companion object {
            fun getVal(context: Context, key: Main.MainKeys): String {
                return PreferencesManager.getInstance(context).getData(key.toString(), savedData.defaultValues[key]!!)
            }
            fun generateCash(context: Context){
                enumValues<MainKeys>().forEach {
                    savedData.cacheValues[it] = getVal(context, it)
                }
            }
        }
    }

    sealed class AppsView{
        enum class AppsViewKeys{
            labelSize,
            labelWidth,
            labelBGBlurValue,
            labelBGTint,
            height,
            width,
            iconSize,
            iconSeparation,
            iconRowSeparation,
            iconBGBlurValue,
            iconBGTint,
            iconBorderSize,
            selectedIconBorderSize,
            appNameBottumPadding,
        }

        object savedData {
            val defaultValues = mutableMapOf<AppsViewKeys,String>(
                    AppsViewKeys.height to "490.0",
                    AppsViewKeys.width to "400.0",

                    AppsViewKeys.labelSize to "30.0",
                    AppsViewKeys.labelWidth to "50.0",
                    AppsViewKeys.labelBGBlurValue to "30.0",
                    AppsViewKeys.labelBGTint to Color.Transparent.toString(),

                    AppsViewKeys.iconSize to "50.0",
                    AppsViewKeys.iconSeparation to "130.0",
                    AppsViewKeys.iconRowSeparation to "130.0",
                    AppsViewKeys.iconBGBlurValue to "20.0",
                    AppsViewKeys.iconBGTint to Color.Transparent.toString(),
                    AppsViewKeys.iconBorderSize to "5.0",
                    AppsViewKeys.selectedIconBorderSize to "20.0",
                    AppsViewKeys.appNameBottumPadding to "100.0",
                )

            val cacheValues = mutableMapOf<AppsViewKeys, String>()
        }



        companion object {
            fun getVal(context: Context, key: AppsViewKeys): String {
                return PreferencesManager.getInstance(context).getData(key.toString(), savedData.defaultValues[key]!!)
            }
            fun generateCash(context: Context){
                enumValues<AppsViewKeys>().forEach {
                    savedData.cacheValues[it] = getVal(context, it)
                }
            }
        }
    }

    fun saveData(context: Context) {
        val preffInstance = PreferencesManager.getInstance(context)
        enumValues<AppsView.AppsViewKeys>().forEach {
            preffInstance.saveData(it.toString(), getString(it))
        }
        enumValues<Main.MainKeys>().forEach {
            preffInstance.saveData(it.toString(), getString(it))
        }
    }

    fun getCashFromSavedData(context: Context){
        val preffInstance = PreferencesManager.getInstance(context)
        enumValues<AppsView.AppsViewKeys>().forEach {
            updateCash(it,preffInstance.getData(it.toString(), getString(it)))
        }
        enumValues<Main.MainKeys>().forEach {
            updateCash(it,preffInstance.getData(it.toString(), getString(it)))
        }
    }

    fun generateCash(context: Context) {
        AppsView.generateCash(context)
        Main.generateCash(context)
    }

    fun updateCash(key: AppsView.AppsViewKeys, value: String){
        AppsView.savedData.cacheValues[key] = value
    }
    fun updateCash(key: Main.MainKeys, value: String){
        Main.savedData.cacheValues[key] = value
    }

    fun getString(key: AppsView.AppsViewKeys): String {
        val x = AppsView.savedData.cacheValues[key]
        return x ?:AppsView.savedData.defaultValues[key]!!
    }
    fun getString(key: Main.MainKeys): String {
        val x = Main.savedData.cacheValues[key]
        return x ?:Main.savedData.defaultValues[key]!!
    }

    fun getFloat(key: AppsView.AppsViewKeys): Float {
        val x = AppsView.savedData.cacheValues[key]?.toFloat()
        return x ?:AppsView.savedData.defaultValues[key]!!.toFloat()
    }
    fun getFloat(key: Main.MainKeys): Float {
        val x = Main.savedData.cacheValues[key]?.toFloat()
        return x?: Main.savedData.defaultValues[key]!!.toFloat()
    }

    fun getDp(key: AppsView.AppsViewKeys, positive: Boolean = true): Dp = getFloat(key).dp
    fun getDp(key: Main.MainKeys, positive: Boolean = true): Dp = getFloat(key).dp

    fun getColor(key: AppsView.AppsViewKeys): Color {
        return Color(android.graphics.Color.parseColor(AppsView.savedData.cacheValues[key] ?: AppsView.savedData.defaultValues[key]!!))
    }
    fun getColor(key: Main.MainKeys): Color {
        return Color(android.graphics.Color.parseColor(Main.savedData.cacheValues[key] ?: Main.savedData.defaultValues[key]!!))
    }

    fun getBoolean(key: AppsView.AppsViewKeys): Boolean {
        return (AppsView.savedData.cacheValues[key] ?: "false") == "true"
    }
    fun getBoolean(key: Main.MainKeys): Boolean {
        return (Main.savedData.cacheValues[key] ?: "false") == "true"
    }
}
