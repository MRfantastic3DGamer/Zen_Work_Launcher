package com.zenworklauncher.screans.home.model

import android.content.Context
import android.content.Intent
import com.dhruv.quick_apps.Action
import com.example.launcher.Drawing.DrawablePainter


data class AppData(
    @JvmField val Name: String,
    val packageName: String,
    val painter: DrawablePainter,
    val launchIntent: Intent?,
    @JvmField var OnSellect:(context: Context)->Unit = {},
) : Action(Name, OnSellect){
    init {

        OnSellect = { context ->
            context.startActivity(launchIntent)
        }
        super.onSelect = OnSellect
    }
}