package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.settings.model.FoldersPageState
import com.zenworklauncher.screans.settings.model.GroupData

@Composable
fun FoldersSettings(state: FoldersPageState) {
    LazyColumn(
        Modifier
            .fillMaxSize(),
    ){
        if (state.folders.isEmpty()){
            item { SettingsHeading(text = "No folders created yet") }
        }

        items(state.folders){
            AppsGroupButton(
                data = it,
                delete = {},
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Row (
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        val newList = mutableListOf(GroupData("new group", Icons.Default.List, mutableListOf()))
                        newList.addAll(state.folders)
                        state.folders = newList
                    },
                horizontalArrangement = Arrangement.Center
            ){
                Icon(imageVector = Icons.Default.Add, contentDescription = "add-new-group")
            }
        }
    }
}