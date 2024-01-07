package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.launcher.Drawing.DrawablePainter
import com.zenworklauncher.model.AppUsageDataEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageDataButton (
    modifier: Modifier = Modifier,
    data: AppUsageDataEntity,
    icon: DrawablePainter,
    update: (AppUsageDataEntity)->Unit,
    groups: List<String>,
){
    var nameBeingEdited by remember { mutableStateOf(false) }
    var currentEditedName by remember { mutableStateOf(data.name) }
    var currentlySelectingGroup by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = modifier,
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Row(
                Modifier,
                Arrangement.Start,
            ) {
                Image(
                    painter = icon,
                    contentDescription = "${data.`package`}-icon",
                    Modifier
                        .size(30.dp),
                    Alignment.Center,
                    ContentScale.Fit,
                )
                when (nameBeingEdited) {
                    true -> TextField(
                        value = currentEditedName,
                        onValueChange = {
                            currentEditedName = it
                        },
                        Modifier,
                        maxLines = 1,
                        singleLine = true,
                        keyboardActions = KeyboardActions(onAny = { update(data.copy(name = currentEditedName)) }),
                    )

                    false -> Text(text = data.name)
                }
                IconButton(onClick = { nameBeingEdited = !nameBeingEdited }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "change-name-icon",
                    )
                }
            }
            Row(
                Modifier,
                Arrangement.End,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "app-visibility-icon",
                    )
                }
                IconButton(onClick = {
                    update(data.copy(hidden = !data.hidden))
                }) {
                    Icon(
                        imageVector = if (data.hidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "app-visibility-icon",
                    )
                }
            }
        }
        if (currentlySelectingGroup){
            AlertDialog(
                onDismissRequest = { currentlySelectingGroup = false },
                confirmButton = { currentlySelectingGroup = false },
                Modifier.fillMaxSize(),
                text = {
                    LazyColumn{
                        items(groups){
                            Card (
                                onClick = {
                                    currentlySelectingGroup = false
                                    update(data.copy(group = it))
                                },
                                shape = RoundedCornerShape(10.dp),
                            ){
                                Text(text = it)
                            }
                        }
                    }
                }
            )
        }
    }
}