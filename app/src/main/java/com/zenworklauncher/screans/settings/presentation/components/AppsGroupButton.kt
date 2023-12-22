package com.zenworklauncher.screans.settings.presentation.components

import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.zenworklauncher.screans.home.model.AppData
import com.zenworklauncher.screans.settings.model.GroupData

@Composable
fun AppsGroupButton (
    data: GroupData,
    delete: ()->Unit
){
    var opened by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Card (
        Modifier
            .clickable {
                opened = !opened
                println("opened: $opened")
                println(data.packages)
            }
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(imageVector = data.icon, contentDescription = "group-${data.name}icon")
            SettingsHeading(text = data.name)
            IconButton(onClick = delete ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete-group")
            }
        }
        if (opened)
            data.packages.forEach { AppDataButton(data = it, delete = {data.packages.remove(it)}) }

        Row (
            Modifier.clickable {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val pm = context.packageManager
                val appsList = pm.queryIntentActivities(intent, 0)
                val appNames = appsList.map { it.loadLabel(pm).toString() }.toTypedArray()

                AlertDialog.Builder(context)
                    .setTitle("Select an app")
                    .setItems(appNames) { dialog, which ->
                        val packageName = appsList[which].activityInfo.packageName
                        Toast.makeText(context, "Selected package: $packageName", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add-new-app"
            )
        }
    }
}

@Composable
fun AppDataButton (data: AppData, delete: () -> Unit){
    Row {
        Image(painter = data.painter, contentDescription = "app-data-for-${data.name}")
        Text(text = data.name)
        IconButton(onClick = delete ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "remove-app-from-group")
        }
    }
}