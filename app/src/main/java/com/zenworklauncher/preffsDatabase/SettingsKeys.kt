package com.zenworklauncher.preffsDatabase

import android.content.Context
import androidx.compose.ui.graphics.Color

sealed class SettingsKeys{

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
        }

        object default {
            val Values
                get() = mapOf<AppsViewKeys,String>(
                    AppsViewKeys.height to "490.0",
                    AppsViewKeys.width to "400.0",

                    AppsViewKeys.labelSize to "20.0",
                    AppsViewKeys.labelWidth to "50.0",
                    AppsViewKeys.labelBGBlurValue to "20.0",
                    AppsViewKeys.labelBGTint to Color.Transparent.toString(),

                    AppsViewKeys.iconSize to "50.0",
                    AppsViewKeys.iconSeparation to "100.0",
                    AppsViewKeys.iconRowSeparation to "130.0",
                    AppsViewKeys.iconBGBlurValue to "20.0",
                    AppsViewKeys.iconBGTint to Color.Transparent.toString(),
                    AppsViewKeys.iconBorderSize to "5.0",
                    AppsViewKeys.selectedIconBorderSize to "15.0",
                )
        }

        fun getVal(context: Context, key: AppsViewKeys): String {
            return PreferencesManager.getInstance(context).getData(key.toString(), default.Values[key]!!)
        }
    }
}
