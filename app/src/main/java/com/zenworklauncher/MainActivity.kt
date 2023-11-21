package com.zenworklauncher

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.home.HomeViewModel
import com.zenworklauncher.screans.home.presentation.HomeScreen
import com.zenworklauncher.screans.settings.SettingsViewModel
import com.zenworklauncher.screans.settings.presentation.SettingsScreen
import com.zenworklauncher.ui.theme.ZenWorkLauncherTheme


class MainActivity : ComponentActivity() {

    private lateinit var context: Context
    private lateinit var pm: PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

//        val appWidgetManager = AppWidgetManager.getInstance(this)
//        val appWidgetHost = AppWidgetHost(this, resources.getInteger(R.integer.host_id))
//        appWidgetHost.allocateAppWidgetId()
//        if(appWidgetManager.bindAppWidgetIdIfAllowed(100, ComponentName())){
//        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
//            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.componentName)
//            // This is the options bundle described in the preceding section.
//            putExtra(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS, options)
//        }
//        }
//        startActivityForResult(intent, REQUEST_BIND_APPWIDGET)

        setContent {

            context = this
            pm = packageManager
            SettingsValues.generateCash(context)


            ZenWorkLauncherTheme {
                var state by remember {
                    mutableStateOf("home")
                }

                val homeVM by remember {
                    mutableStateOf(HomeViewModel(pm, context))
                }

                val settingsVM by remember {
                    mutableStateOf(SettingsViewModel(homeVM))
                }


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
                    viewModel = homeVM
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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZenWorkLauncherTheme {
        Greeting("Android")
    }
}


// region app drawr
//            val allApps by remember { mutableStateOf(getAllAppsFromPackageManager(pm)) }
//            val foldersByPackages by remember { mutableStateOf(getFoldersByPackageSimilarities(allApps)) }
//            val folderButtons by remember { mutableStateOf(getAllFolderButtons(0,foldersByPackages,separation,buttonSize,rowSize)) }
//            val appButtons by remember { mutableStateOf(getAllAppButtons(folderButtons.size,allApps,separation,buttonSize,rowSize)) }
//            val actionMap by remember { mutableStateOf(getPositionActions(appButtons,folderButtons,rowSize)) }
//            val onClickAction : (Int)->Unit by remember{
//                mutableStateOf({index ->
//                    if (actionMap[index] != null) {
//                        if (actionMap[index]?.folderIndex == null) {
//                            val launchIntent: Intent? =
//                                actionMap[index]?.let { pm.getLaunchIntentForPackage(it.appPackage) }
//                            if (launchIntent != null) {
//                                ContextCompat.startActivity(context, launchIntent, Bundle.EMPTY)
//                            } else {
//                                println("could not open this app : ${actionMap[index]?.appPackage}")
//                            }
//                        } else {
//                            // TODO: open folder
//                        }
//                    }
//                })
//            }
// endregion