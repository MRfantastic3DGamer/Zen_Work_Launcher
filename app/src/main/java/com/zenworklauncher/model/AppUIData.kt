package com.zenworklauncher.model

import android.content.Intent
import androidx.compose.runtime.Immutable
import com.dhruv.quick_apps.Action
import com.example.launcher.Drawing.DrawablePainter

@Immutable
data class AppUIData(
    @JvmField val Name: String,
    val packageName: String,
    val painter: DrawablePainter,
    val launchIntent: Intent?,
) : Action(Name){}