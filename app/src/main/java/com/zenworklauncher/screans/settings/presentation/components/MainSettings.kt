package com.zenworklauncher.screans.settings.presentation.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zenworklauncher.database.preffs_database.SettingsValues
import com.zenworklauncher.screans.settings.SettingsViewModel

@Composable
fun MainSettings(viewModel: SettingsViewModel) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LazyColumn(
        content = {
            item { SettingsHeading(text = "Wallpaper") }
            item {
                Button(onClick = { launcher.launch("image/*") }) {
                    Text(text = "Pick image")
                }
            }
            item {
                var value by remember { mutableStateOf(SettingsValues.getBoolean(SettingsValues.Main.MainKeys.HapticsEnabled)) }
                Button(
                    onClick = {
                        value = !value
                        SettingsValues.updateCash(SettingsValues.Main.MainKeys.HapticsEnabled, value.toString())
                    }
                ) {
                    Text(text = if(value) "Feel Haptics" else "Don't Feel Haptics")
                }
            }
            item {

            }
            item { Box (Modifier.height(600.dp)) }
        }
    )
}