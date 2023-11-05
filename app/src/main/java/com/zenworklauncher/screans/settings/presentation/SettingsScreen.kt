package com.zenworklauncher.screans.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        @Composable
        fun tabButton(
            text: String,
            onTap: ()->Unit
        ){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .clickable { onTap() }
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .vertical(true)
                        .rotate(-90f)
                        .padding(20.dp)
                )
            }
        }

        Row(
            Modifier.fillMaxSize()
        ) {
            NavigationRail(
                modifier = Modifier.width(50.dp),
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                windowInsets = WindowInsets(0,0,0,0),
                header = {
                    Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "back")
                },
                content = {
                    tabButton(text = "Theme"){

                    }
                    tabButton(text = "Haptics") {

                    }
                    tabButton(text = "Apps") {

                    }
                    tabButton(text = "Folders") {

                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }
}


fun Modifier.vertical(enabled: Boolean = true) =
    if (enabled)
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
            layout(placeable.height, placeable.width) {
                placeable.place(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2)
                )
            }
        } else this