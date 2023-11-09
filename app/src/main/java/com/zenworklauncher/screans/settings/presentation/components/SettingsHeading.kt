package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun SettingsHeading(text: String, scrollState: LazyListState? = null){

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 50.dp, 10.dp, 10.dp),
        text = text,
        style = TextStyle(
            color = Color.Black,
            fontSize = TextUnit(30f, TextUnitType.Sp),
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Left
        )
    )
}