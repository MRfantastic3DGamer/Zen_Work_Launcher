package com.zenworklauncher.screans.settings.presentation

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.zenworklauncher.model.GroupDataEntity
import com.zenworklauncher.screans.settings.FoldersPageState
import com.zenworklauncher.screans.settings.presentation.components.AppsGroupButton
import com.zenworklauncher.screans.settings.presentation.components.SettingsHeading

@Composable
fun GroupsSettings(
    state: FoldersPageState,
    upsertGroup: (GroupDataEntity?, GroupDataEntity) -> Unit,
    deleteGroup: (GroupDataEntity)->Unit,
) {
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
                delete = { deleteGroup(it) },
                update = { new -> upsertGroup(it, new) },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Row (
                Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = "add new group", role = Role.Button, onClick = {
                        upsertGroup(
                            null,
                            GroupDataEntity(
                                name = "new group",
                                animatedIconKey = "",
                            )
                        )
                    }),
                horizontalArrangement = Arrangement.Center
            ){
                Icon(imageVector = Icons.Default.Add, contentDescription = "add-new-group")
            }
        }
    }
}