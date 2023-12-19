package com.zenworklauncher.screans.home.widgetssection

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AppWidgetHostView(
    modifier: Modifier = Modifier,
    appWidgetId: Int,
    appWidgetHost: AppWidgetHost,
    appWidgetManager: AppWidgetManager
) {
    val appWidgetHostView = appWidgetHost.createView(
        LocalContext.current,
        appWidgetId,
        appWidgetManager.getAppWidgetInfo(appWidgetId)
    )

    DisposableEffect(appWidgetHostView) {
        onDispose {
            appWidgetHost.deleteAppWidgetId(appWidgetId)
        }
    }

    // Measure the app widget size
    appWidgetHostView.measure(
        View.MeasureSpec.makeMeasureSpec(100.dp.value.toInt(), View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(100.dp.value.toInt(), View.MeasureSpec.EXACTLY)
    )

    // Layout the app widget
    appWidgetHostView.layout(
        0,
        0,
        appWidgetHostView.measuredWidth,
        appWidgetHostView.measuredHeight
    )

    AndroidView(
        factory = { appWidgetHostView },
        modifier = modifier
    )
}