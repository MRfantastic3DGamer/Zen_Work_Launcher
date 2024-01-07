package com.zenworklauncher

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.database.rooms_database.DatabaseProvider
import com.zenworklauncher.screans.home.HomeViewModel
import com.zenworklauncher.screans.home.presentation.HomeScreen
import com.zenworklauncher.screans.settings.SettingsViewModel
import com.zenworklauncher.screans.settings.presentation.SettingsScreen
import com.zenworklauncher.ui.theme.ZenWorkLauncherTheme


class MainActivity : ComponentActivity() {

    private lateinit var context: Context
    private lateinit var pm: PackageManager
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var _viewModel: MutableState<HomeViewModel>
    val viewModel
        get() = _viewModel.value

    override fun onStart() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { res -> println(res) }
        super.onStart()
        pm = packageManager
        context = this
        DatabaseProvider.initialize(pm, context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            context = this
            pm = packageManager
            SettingsValues.generateCash(context)


            ZenWorkLauncherTheme {
                var state by remember { mutableStateOf("home") }

                _viewModel = remember {
                    mutableStateOf(
                        HomeViewModel(
                            packageManager =  pm,
                            context = context,
                            appWidgetHost = AppWidgetHost(context, APP_WIDGET_HOST_ID),
                            appWidgetManager = AppWidgetManager.getInstance(context)
                        )
                    )
                }

                val settingsVM by remember { mutableStateOf(SettingsViewModel(viewModel)) }

                LaunchedEffect(Unit){
                    SettingsValues.getCashFromSavedData(context)
                }

                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    if (!SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled))
                                        return@detectTapGestures
                                    val vibratorManager =
                                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                                    val vibrator = vibratorManager.defaultVibrator
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(
                                            VibrationEffect.EFFECT_HEAVY_CLICK
                                        )
                                    )
                                    state = "settings"
                                }
                            )
                        },
                    viewModel = viewModel
                )

                AnimatedVisibility(visible = state == "settings") {
                    SettingsScreen(
                        viewModel = settingsVM,
                        backFunction = {
                            state = "home"
                        }
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                if (data != null) {
                     configureWidget(data)
                }
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                if (data != null) {
                    viewModel.createWidget(data)
                }
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                viewModel.appWidgetHost.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    fun selectWidget() {
        val appWidgetId: Int = viewModel.appWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo: ArrayList<out Parcelable> = ArrayList()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras: ArrayList<out Parcelable> = ArrayList()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    }

    private fun configureWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo: AppWidgetProviderInfo = viewModel.appWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
        } else {
            viewModel.createWidget(data)
        }
    }

    companion object {
        const val APP_WIDGET_HOST_ID = 1
        const val REQUEST_PICK_APPWIDGET = 2
        const val REQUEST_CREATE_APPWIDGET = 3
    }
}