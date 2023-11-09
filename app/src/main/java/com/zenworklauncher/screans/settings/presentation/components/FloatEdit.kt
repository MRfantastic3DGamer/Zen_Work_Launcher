package com.zenworklauncher.screans.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.zenworklauncher.preffsDatabase.SettingsValues
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun FloatEdit(
    name: String,
    subtitle: String,
    key: SettingsValues.AppsView.AppsViewKeys,
    min: Float,
    max: Float,
    scrollState: LazyListState,
) {
    var value by remember { mutableStateOf(SettingsValues.getFloat(key)) }
    var startedDragging by remember { mutableStateOf(false) }
    var valueEditingMode by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startedDragging = true },
                    onDragEnd = {
                        startedDragging = false
                        valueEditingMode = false
                    },
                    onDragCancel = {
                        startedDragging = false
                        valueEditingMode = false
                    },
                )
                { change, dragAmount ->
                    if (startedDragging) {
                        valueEditingMode = dragAmount.x.absoluteValue > dragAmount.y.absoluteValue
                        startedDragging = false
                    }
                    if (valueEditingMode) {
                        value = clamp(value + dragAmount.x / 50, min, max)
                        SettingsValues.updateCash(key, value.toString())
                        change.consume()
                    } else {
                        scope.launch {
                            scrollState.scrollBy(-dragAmount.y)
                        }
                    }
                }
            }
            .padding(5.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(7.dp)),
    )
    {
        Row(
            Modifier.fillMaxWidth()
                .padding(10.dp),
            Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, style = TextStyle(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = TextStyle(fontWeight = FontWeight.Light))
            }
            val x = value*100
            val y = x.roundToInt()/100f
            Text(text = y.toString())
        }
    }
}