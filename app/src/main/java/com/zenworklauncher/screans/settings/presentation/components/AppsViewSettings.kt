package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.settings.SettingsViewModel

@Composable
fun AppsViewSettings(viewModel: SettingsViewModel) {
    val scrollState by remember { mutableStateOf(LazyListState()) }
    LazyColumn(
        modifier = Modifier,
        state = scrollState,
        content = {
            item {
                SettingsHeading(text = "Main", scrollState)
            }
            item{
                FloatEdit(
                    name = "height",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.height,
                    min = 400f,
                    max = 700f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "label width",
                    subtitle = "with of the labels section",
                    key = SettingsValues.AppsView.AppsViewKeys.labelWidth,
                    min = 20f,
                    max = 70f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "selected app name padding",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.appNameBottumPadding,
                    min = 0f,
                    max = 500f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item {
                SettingsHeading(text = "Theme", scrollState)
            }
            item{
                FloatEdit(
                    name = "labelSize",
                    subtitle = "the size of the label",
                    key = SettingsValues.AppsView.AppsViewKeys.labelSize,
                    min = 0f,
                    max = 50f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "icon size",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.iconSize,
                    min = 20f,
                    max = 100f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "icon separation",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.iconSeparation,
                    min = 20f,
                    max = 200f,
                    scrollState = scrollState,
                    onChange = {
                        viewModel.shouldRebuildIconPositions = true
                    },
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "icon row separation",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.iconRowSeparation,
                    min = 20f,
                    max = 200f,
                    scrollState = scrollState,
                    onChange = {
                        viewModel.shouldRebuildIconPositions = true
                    },
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "icon border size",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.iconBorderSize,
                    min = 0f,
                    max = 50f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item{
                FloatEdit(
                    name = "selected icon size",
                    subtitle = "",
                    key = SettingsValues.AppsView.AppsViewKeys.selectedIconBorderSize,
                    min = 0f,
                    max = 50f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
            }
            item {
                SettingsHeading(text = "glass effect", scrollState)
            }
            item{
                FloatEdit(
                    name = "label BG Blur Value",
                    subtitle = "increase it fore more of a glass effect",
                    key = SettingsValues.AppsView.AppsViewKeys.labelBGBlurValue,
                    min = 0f,
                    max = 100f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
                Text(text = "label BG tint")
            }
            item{
                FloatEdit(
                    name = "icons BG Blur Value",
                    subtitle = "increase it fore more of a glass effect",
                    key = SettingsValues.AppsView.AppsViewKeys.iconBGBlurValue,
                    min = 0f,
                    max = 100f,
                    scrollState = scrollState,
                    state = viewModel.currentlyBeingEdited,
                )
                Text(text = "icons BG tint")
            }
            item { Box (Modifier.height(600.dp)) }
        }
    )
    Column {
    }
}