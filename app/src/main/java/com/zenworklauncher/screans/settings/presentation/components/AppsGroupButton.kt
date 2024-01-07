package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.GroupDataEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsGroupButton (
    data: GroupDataEntity,
    delete: ()->Unit,
    update: (GroupDataEntity)->Unit
){
    var opened by remember { mutableStateOf(false) }
    var groupName by remember{ mutableStateOf(data.name) }

    val context = LocalContext.current

    Card (
        Modifier
            .clickable {
                opened = !opened
            }
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
//            Icon(imageVector = data.animatedIconKey, contentDescription = "group-${data.name}icon")
//            SettingsHeading(text = data.name)
            TextField(value = groupName, onValueChange = {
                groupName = it
                val newGroup = data.copy(name = it)
                update(newGroup)
            })
            IconButton(onClick = delete ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete-group")
            }
        }
    }
}

@Composable
fun AppDataButton (painter: DrawablePainter? = null, name: String, delete: () -> Unit){
    Row {
        Placeholder(width = TextUnit(value = 20f, type = TextUnitType.Sp), height = TextUnit(value = 20f, type = TextUnitType.Sp), placeholderVerticalAlign = PlaceholderVerticalAlign.Center)
//        Image(painter = painter, contentDescription = "app-data-for-$name")
        Text(text = name)
        IconButton(onClick = delete ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "remove-app-from-group")
        }
    }
}