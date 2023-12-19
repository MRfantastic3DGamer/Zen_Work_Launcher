package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.quick_apps.Action
import com.zenworklauncher.preffsDatabase.SettingsValues

@Composable
fun SelectedAppLabel(action: Action?){
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp,0.dp,0.dp, SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.appNameBottumPadding)),
        text = action?.name ?: "",
        style = TextStyle(
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(35f, TextUnitType.Sp)
        )
    )
}