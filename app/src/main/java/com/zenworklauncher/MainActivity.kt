package com.zenworklauncher

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.settings.SettingsViewModel
import com.zenworklauncher.screans.settings.presentation.SettingsScreen
import com.zenworklauncher.ui.theme.ZenWorkLauncherTheme

class MainActivity : ComponentActivity() {

    private lateinit var context: Context
    private lateinit var pm: PackageManager

    private val separation = 240
    private val buttonSize = 120.dp
    private val rowSize    = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            context = this
            pm = packageManager




            ZenWorkLauncherTheme {

                SettingsScreen(
                    viewModel = SettingsViewModel()
                )
//                HomeScreen(
//                    viewModel = HomeViewModel(pm, context)
//                )

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